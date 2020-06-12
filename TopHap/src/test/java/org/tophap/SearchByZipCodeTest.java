package org.tophap;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.tophap.helpers.UserHelper;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.LoginPage;
import org.tophap.model.pages.MapPage;
import org.tophap.runner.MultipleWebTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.tophap.model.api.SearchSortFilter.SearchItem;

public class SearchByZipCodeTest extends MultipleWebTest {

    private static final String ZIP_CODE1 = "94024";
    private static final String ZIP_CODE2 = "94518";
    private static final String BODY1 = "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.17183890850436,37.326302031836164],[-122.02941709149604,37.388474345264356]],\"zones\":[\"000394024\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[\"Active\"],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}";
    private static final String BODY2 = "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.11114292160038,37.92326853694969],[-122.0442410783999,37.98438186084827]],\"zones\":[\"000394518\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[\"Active\"],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}";

    private List<SearchItem> searchItemList = new ArrayList<>();
    private List<SearchItem> searchItemListOnServer = new ArrayList<>();

    private static Stream<Arguments> zipBodyParameters() {
        return Stream.of(
                Arguments.of(ZIP_CODE1, BODY1),
                Arguments.of(ZIP_CODE2, BODY2)
        );
    }

    @Order(1)
    @Test
    void loginTest() {
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.openLogin();
        loginPage.login(UserHelper.EMAIL, UserHelper.PASSWORD);
        homePage.closeEmailConfirmationFailureMsg();
        homePage.tryForFreeStart();
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource("zipBodyParameters")
    void returnedResultsAreInSearchedZipCodeArea(String zip, String body) throws InterruptedException {

        MapPage mapPage = new MapPage(getDriver());
        mapPage.submitSearch(zip);
        mapPage.applyActivePropertyStatusFilter();

        searchItemList = mapPage.getListItemFromSearchResult();
        assertEquals(
                searchItemList.size(),
                searchItemList.stream()
                        .map(SearchItem::getZipCode)
                        .filter(zip::equals)
                        .count());

        assertTrue(searchItemList.size() > 0, "No items in search results");
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource("zipBodyParameters")
    void serverResultsHaveSubmittedZipCode(String zip, String body) throws IOException {

        searchItemListOnServer = SearchSortFilter.getSearchItemsList(body);

        assertEquals(searchItemListOnServer.size(),
                searchItemListOnServer.stream()
                        .map(SearchItem::getZipCode)
                        .filter(zip::equals)
                        .count());
    }

    @Test
    @Order(4)
    void resultsOnClientAndServerMatch_HttpClientInterface() throws IOException {

        assertTrue(searchItemList.containsAll(searchItemListOnServer)
                && searchItemListOnServer.containsAll(searchItemList));
    }

    @Order(5)
    @Test
    void resultsOnClientAndServerMatch_RestAssuredInterface() throws IOException {

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(BODY2)
                .when()
                .post("https://staging-api.tophap.com/properties/search")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("items._source.address.UnparsedAddress",
                        Matchers.hasItems(searchItemList.stream().map(SearchItem::getAddress).collect(Collectors.toList()).toArray()));
    }
}