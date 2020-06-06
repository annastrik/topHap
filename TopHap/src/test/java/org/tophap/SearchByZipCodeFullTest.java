package org.tophap;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchByZipCodeFullTest extends MultipleWebTest {

    private static final String ZIP_CODE = "94518";
    private static final String BODY = "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.11114292160038,37.92326853694969],[-122.0442410783999,37.98438186084827]],\"zones\":[\"000394518\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[\"Active\"],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}";

    private List<SearchSortFilter.SearchItem> searchItemList = new ArrayList<>();
    private List<SearchSortFilter.SearchItem> sortedSearchItemList = new ArrayList<>();
    private List<SearchSortFilter.SearchItem> searchItemListOnServer = new ArrayList<>();

    private String getZipFromRegion(String region) {
        return region.substring(region.length() - 5);
    }

    private String getCityFromRegion(String region) {
        return region.substring(0, region.length() - 10).toUpperCase();
    }

    private String getAddressChangeFormat(String address) {
        return address.replace("Apt", "").replace("Unit", "").toUpperCase();
    }

    private String getStatusChangeFormat(String status) {
        return status.replace("NEW", "Active").replace("ACTIVE", "Active");
    }

    @Disabled
    @Test
    @Order(1)
    void returnedResultsAreInSearchedZipCodeArea() throws InterruptedException {

        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.openLogin();
        loginPage.login(UserHelper.EMAIL, UserHelper.PASSWORD);
        homePage.closeEmailConfirmationFailureMsg();

        MapPage mapPage = homePage.tryForFreeStart();
        mapPage.submitSearch(ZIP_CODE);
        mapPage.applyActivePropertyStatusFilter();

        int searchResultsCountOnClient = mapPage.forEachItemInSearchResult(
                element -> {
                    WebElement region = MapPage.getRegionFromSearchItemResult(element);
                    String zipCode = getZipFromRegion(region.getText());
                    assertEquals(ZIP_CODE, zipCode);
                    String city = getCityFromRegion(region.getText());
                    String address = getAddressChangeFormat(element.findElement(MapPage.ADDRESS_LOCATOR).getText());
                    int price = MapPage.getPriceFromSearchItemResult(element);
                    String status = getStatusChangeFormat(element.findElement(MapPage.STATUS_LOCATOR).getText());
                    searchItemList.add(new SearchSortFilter.SearchItemPOJO(price, address, zipCode, city, status));
                });

        assertTrue(searchResultsCountOnClient > 0, "No items in search results");
        // todo Sergei has to explain how to sort objects, then the test will be fixed and @disabled will be removed
        sortedSearchItemList = searchItemList.stream().sorted().collect(Collectors.toList());
    }

    @Disabled
    @Test
    @Order(2)
    void serverResultsHaveSubmittedZipCode() throws IOException {

        searchItemListOnServer = SearchSortFilter.getSearchItemsList(BODY);

        List<String> itemsWithWrongZipCode = searchItemListOnServer
                .stream()
                .map(SearchSortFilter.SearchItem::getZipCode)
                .filter(x -> !ZIP_CODE.equals(x))
                .collect(Collectors.toList());
        assertEquals(0, itemsWithWrongZipCode.size());
    }

    @Disabled
    @Test
    @Order(3)
    void resultsInClientAndServerMatch_HttpClientInterface() throws IOException {

        // todo Sergei has to explain how to sort objects, then the test will be fixed and @disabled will be removed
        List<SearchSortFilter.SearchItem> sortedSearchItemListOnServer = searchItemListOnServer.stream().sorted().collect(Collectors.toList());
        assertEquals(sortedSearchItemList, sortedSearchItemListOnServer);
    }

    @Order(4)
    @Test
    void resultsInClientAndServerMatch_RestAssuredInterface() throws IOException {

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(BODY)
                .when()
                .post("https://staging-api.tophap.com/properties/search")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("items._source.address.UnparsedAddress", Matchers.hasItems(sortedSearchItemList.toArray()));
    }
}