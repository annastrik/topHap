package org.tophap;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SearchByZipCodeTest extends MultipleWebTest {

    private static final By REGION_LOCATOR = By.cssSelector(".th-region");
    private static final String BODY = "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.16544265011063,37.92303606693025],[-122.04209836280647,37.98461413680742]],\"zones\":[\"000394523\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[\"Active\"],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}";
    private List<String> searchResultsListOnClient = new ArrayList<>();

    private String getZipFromRegion(String region) {
        return region.substring(region.length() - 5);
    }

    @Test
    @Order(1)
    void returnedResultsAreInSearchedZipCodeArea() throws InterruptedException {

        List<String> searchResultsList = new ArrayList<>();

        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.openLogin();
        loginPage.login(UserHelper.EMAIL, UserHelper.PASSWORD);
        homePage.closeEmailConfirmationFailureMsg();

        MapPage mapPage = homePage.tryForFreeStart();
        mapPage.submitSearch(MapPage.ZIP_TEST);
        mapPage.applyActivePropertyStatusFilter();

        int searchResultsCountOnClient = mapPage.forEachItemInSearchResult(
                element -> {
                    WebElement currentElement = element.findElement(REGION_LOCATOR);
                    String zipCode = getZipFromRegion(currentElement.getText());
                    assertEquals(MapPage.ZIP_TEST, zipCode);
                    searchResultsList.add(element.findElement(By.cssSelector(".th-address")).getText());
                });

        assertTrue(searchResultsCountOnClient > 0, "No items in search results");
        searchResultsListOnClient = mapPage.getSortedAddressesList(searchResultsList);
    }

    @Test
    @Order(2)
    void returnedResultsListFromServerMatchesResultsInClient() throws IOException {

        List<String> searchResultsListOnServer = SearchSortFilter.getSearchItemsList(BODY).stream()
                .map(SearchSortFilter.SearchItem::getAddress)
                .sorted()
                .collect(Collectors.toList());
        assertEquals(searchResultsListOnClient, searchResultsListOnServer);
    }

    @Test
    @Order(3)
    void distinctZipCodeCountIsEqualOneAndMatchesSubmittedInSearch() throws IOException {

        Set<String> searchResultsSetOnServer = SearchSortFilter.getSearchItemsSet(BODY);
        assertEquals(1, searchResultsSetOnServer.size());
        assertEquals(MapPage.ZIP_TEST, searchResultsSetOnServer.stream().findFirst().get());
    }
}