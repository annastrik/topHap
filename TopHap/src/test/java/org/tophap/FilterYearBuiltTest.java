package org.tophap;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.MapPage;
import org.tophap.runner.MultipleWebTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.tophap.model.api.SearchSortFilter.SearchItem;

public class FilterYearBuiltTest extends MultipleWebTest {

    private static final String ZIP_CODE = "94024";
    private static final String YEAR = "2010";
    private static final int YEAR_INT = Integer.parseInt(YEAR);

    private static final String BODY = "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.20580563727256,37.30873431299226],[-122.07485136272709,37.40953942032269]],\"zones\":[\"000394024\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{\"min\":2010}}}}";

    private List<String> addressesListOnClient = new ArrayList<>();
    private List<SearchItem> searchItemListOnServer = new ArrayList<>();

    @Test
    @Order(1)
    void returnedResultsAreInSearchedZipCodeArea() throws InterruptedException {

        List<String> addressesList = new ArrayList<>();

        HomePage homePage = new HomePage(getDriver());
        MapPage mapPage = homePage.tryForFreeStart();
        mapPage.submitSearch(ZIP_CODE);
        mapPage.applyYearBuiltFilter(YEAR);

        int searchResultsCountOnClient = mapPage.forEachItemInSearchResult(
                element -> {
                    int yearBuilt = MapPage.getYearBuiltFromSearchItemResult(element);
                    assertTrue(yearBuilt >= YEAR_INT);
                    addressesList.add(MapPage.getAddressFromSearchItemResult(element));
                });

        assertTrue(searchResultsCountOnClient > 0, "No items in search results");
        addressesListOnClient = addressesList.stream().sorted().collect(Collectors.toList());
    }

    @Test
    @Order(2)
    void serverResultsHaveSubmittedYearBuilt() throws IOException {

        searchItemListOnServer = SearchSortFilter.getSearchItemsList(BODY);

        List<Integer> itemsWithWrongYear = searchItemListOnServer.stream()
                .map(SearchItem::getYearBuilt)
                .filter(x -> x < YEAR_INT)
                .collect(Collectors.toList());
        assertEquals(0, itemsWithWrongYear.size());
    }

    @Test
    @Order(3)
    void resultsOnClientAndServerMatch_HttpClientInterface() throws IOException {

        List<String> addressesListOnServer = searchItemListOnServer
                .stream()
                .map(SearchItem::getAddress)
                .sorted()
                .collect(Collectors.toList());
        assertEquals(addressesListOnServer, addressesListOnClient);
    }

    @Test
    @Order(4)
    void resultsOnClientAndServerMatch_RestAssuredInterface() throws IOException {

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(BODY)
                .when()
                .post("https://staging-api.tophap.com/properties/search")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("items._source.address.UnparsedAddress", Matchers.hasItems(addressesListOnClient.toArray()));
    }
}