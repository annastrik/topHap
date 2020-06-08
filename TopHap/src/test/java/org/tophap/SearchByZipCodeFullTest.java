package org.tophap;

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
    private List<SearchSortFilter.SearchItem> searchItemListOnServer = new ArrayList<>();

    private String getZipFromRegion(String region) {
        return region.substring(region.length() - 5);
    }

    private String getCityFromRegion(String region) {
        return region.substring(0, region.length() - 10).toUpperCase();
    }

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
                    String address = MapPage.getAddressFromSearchItemResult(element);
                    int price = MapPage.getPriceFromSearchItemResult(element);
                    String status = MapPage.getStatusFromSearchItemResult(element);
                    searchItemList.add(new SearchSortFilter.SearchItemPOJO(price, address, zipCode, city, status));
                });

        assertTrue(searchResultsCountOnClient > 0, "No items in search results");
    }

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

    @Test
    @Order(3)
    void resultsOnClientAndServerMatch_HttpClientInterface() throws IOException {

        assertTrue(searchItemList.containsAll(searchItemListOnServer)
                && searchItemListOnServer.containsAll(searchItemList));
    }
}