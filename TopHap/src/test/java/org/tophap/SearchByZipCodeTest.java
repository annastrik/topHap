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
import org.tophap.helpers.TestHelper;
import org.tophap.helpers.UserHelper;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.LoginPage;
import org.tophap.model.pages.MapPage;
import org.tophap.runner.MultipleWebTest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.tophap.model.api.SearchSortFilter.SearchItem;

public class SearchByZipCodeTest extends MultipleWebTest {

    private static final String[][] params = {
            {"94024", "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.17183890850436,37.326302031836164],[-122.02941709149604,37.388474345264356]],\"zones\":[\"000394024\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[\"Active\"],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}"},
            {"94518", "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.11114292160038,37.92326853694969],[-122.0442410783999,37.98438186084827]],\"zones\":[\"000394518\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[\"Active\"],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}"}};

    private final Map<String, List<SearchItem>> searchItemsMap = new HashMap<>();
    private final Map<String, List<SearchItem>> searchItemsMapOnServer = new HashMap<>();

    private static Stream<String> getZipCodesForTest() {
        return Arrays.stream(params).map(arr -> arr[0]);
    }

    private static Stream<Arguments> getAllArgsForTest() {
        return Arrays.stream(params).map(arr -> Arguments.of(arr[0], arr[1]));
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
    @MethodSource("getZipCodesForTest")
    void returnedResultsAreInSearchedZipCodeArea(String zip) throws InterruptedException {

        MapPage mapPage = new MapPage(getDriver());
        mapPage.submitSearch(zip);
        mapPage.applyActivePropertyStatusFilter();

        List<SearchItem> list = mapPage.getListItemFromSearchResult();
        searchItemsMap.put(zip, list);

        assertEquals(
                list.size(),
                list.stream()
                        .map(SearchItem::getZipCode)
                        .filter(zip::equals)
                        .count());

        assertTrue(list.size() > 0, "No items in search results");
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource("getAllArgsForTest")
    void serverResultsHaveSubmittedZipCode(String zip, String body) throws IOException {

        List<SearchItem> list = SearchSortFilter.getSearchItemsList(body);
        searchItemsMapOnServer.put(zip, list);

        assertEquals(list.size(),
                list.stream()
                        .map(SearchItem::getZipCode)
                        .filter(zip::equals)
                        .count());

        assertTrue(list.size() > 0, "No items in search results");
    }

    @Test
    @Order(4)
    void resultsOnClientAndServerMatch_HttpClientInterface() throws IOException {
        assertTrue(TestHelper.compareLists(
                new ArrayList<>(searchItemsMap.keySet()),
                new ArrayList<>(searchItemsMapOnServer.keySet())));

        for (String key : searchItemsMap.keySet()) {
            assertTrue(TestHelper.compareLists(
                searchItemsMap.get(key),
                searchItemsMapOnServer.get(key)));
        }
    }

    @Order(5)
    @ParameterizedTest
    @MethodSource("getAllArgsForTest")
    void resultsOnClientAndServerMatch_RestAssuredInterface(String zip, String body) throws IOException {

        List<SearchItem> list = searchItemsMap.get(zip);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("https://staging-api.tophap.com/properties/search")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("items._source.address.UnparsedAddress",
                        Matchers.hasItems(list.stream().map(SearchItem::getAddress).toArray()));
    }
}