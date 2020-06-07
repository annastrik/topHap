package org.tophap;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.tophap.helpers.UserHelper;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.runner.MultipleWebTest;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.LoginPage;
import org.tophap.model.pages.MapPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SearchByCityTest extends MultipleWebTest {

    public static final String CITY_NAME = "Pleasant Hill";
    private static final String BODY = "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.17327731067944,37.91763649537775],[-122.04624234304181,37.98966282341976]],\"zones\":[\"00040657764\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[\"Active\"],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}";

    private List<SearchSortFilter.SearchItem> searchItemList = new ArrayList<>();

    private String getCityFromRegion(String region) {
        return region.substring(0, region.length() - 10);
    }
    private String getZipFromRegion(String region) {
        return region.substring(region.length() - 5);
    }

    private String getAddressChangeFormat(String address) {
        return address.replace("Apt", "").replace("Unit", "").toUpperCase();
    }

    private String getStatusChangeFormat(String status) {
        return status.replace("NEW", "Active").replace("ACTIVE", "Active");
    }

    @Test
    @Order(1)
    void searchByCity() throws InterruptedException {

        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.openLogin();
        loginPage.login(UserHelper.EMAIL, UserHelper.PASSWORD);
        homePage.closeEmailConfirmationFailureMsg();
        MapPage mapPage = homePage.tryForFreeStart();
        mapPage.submitSearch(CITY_NAME);
        mapPage.applyActivePropertyStatusFilter();

        int searchResultsCountOnClient = mapPage.forEachItemInSearchResult(
                element -> {
                    WebElement region = MapPage.getRegionFromSearchItemResult(element);
                    String city = getCityFromRegion(region.getText());
                    String address = getAddressChangeFormat(element.findElement(MapPage.ADDRESS_LOCATOR).getText());
                    String zipCode = getZipFromRegion(region.getText());
                    String status = getStatusChangeFormat(element.findElement(MapPage.STATUS_LOCATOR).getText());
                    int price = MapPage.getPriceFromSearchItemResult(element);
                    assertEquals(CITY_NAME, city);

                    searchItemList.add(new SearchSortFilter.SearchItemPOJO(price, address, zipCode, city.toUpperCase(), status));
                });

        assertTrue(searchResultsCountOnClient > 0, "No items in search results");
    }

    @Test
    @Order(2)
    void allItemsReturnedFromServerHaveSubmittedCity() throws IOException {

        final String CITY_NAME_UPPER_CASE = CITY_NAME.toUpperCase();

        List<String> itemsWithWrongCity = SearchSortFilter.getSearchItemsList(BODY).stream()
                .map(SearchSortFilter.SearchItem::getCity)
                .filter(x -> !CITY_NAME_UPPER_CASE.equals(x))
                .collect(Collectors.toList());
        assertEquals(0, itemsWithWrongCity.size());

        List<SearchSortFilter.SearchItem> searchItemsForFilter = SearchSortFilter.getSearchItemsList(BODY);
        assertTrue(searchItemList.containsAll(searchItemsForFilter)
                && searchItemsForFilter.containsAll(searchItemList));
    }
}