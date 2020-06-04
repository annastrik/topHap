package org.tophap;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.runner.MultipleWebTest;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.MapPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SortResultTest extends MultipleWebTest {

    private static final String ZIP_CODE = "94523";
    private static final String BODY = "{\"size\":500,\"sort\":[{\"option\":\"id\",\"dir\":\"asc\"}],\"filters\":{\"bounds\":[[-122.13936414365219,37.923036066930806],[-122.016019856348,37.98461413680798]],\"zones\":[\"000394523\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[\"Active\"],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}";

    private List<Integer> searchResultsListAZ = new ArrayList<>();
    private List<Integer> searchResultsListZA = new ArrayList<>();

    @Test
    @Order(1)
    void loginTest() {
        HomePage homePage = new HomePage(getDriver());
        homePage.tryForFreeStart();
    }

    @Test
    @Order(2)
    void sortAZPriceTest() throws InterruptedException {

        MapPage mapPage = new MapPage(getDriver());
        mapPage.submitSearchApplySortingAndFiltersAZ(ZIP_CODE);

        int[] prevPrice = {Integer.MIN_VALUE};
        int count = mapPage.forEachItemInSearchResult(
                element -> {
                    int currentPrice = MapPage.getPriceFromSearchItemResult(element);
                    assertTrue(prevPrice[0] <= currentPrice);
                    prevPrice[0] = currentPrice;

                    searchResultsListAZ.add(currentPrice);
                });

        assertTrue(count > 0, "No items in search results");
    }

    @Test
    @Order(3)
    void sortZAPriceTest() throws InterruptedException {

        MapPage mapPage = new MapPage(getDriver());
        mapPage.submitSearchApplySortingAndFiltersZA(ZIP_CODE);

        int[] prevPrice = {Integer.MAX_VALUE};

        int count = mapPage.forEachItemInSearchResult(
                element -> {
                    int currentPrice = MapPage.getPriceFromSearchItemResult(element);
                    assertTrue(prevPrice[0] >= currentPrice);
                    prevPrice[0] = currentPrice;

                    searchResultsListZA.add(currentPrice);
                });

        assertTrue(count > 0, "No items in search results");
    }

    @Test
    @Order(4)
    void returnedResultFromServerMatchesResultInClient() throws IOException {

        List<Integer> searchResultsListOnServer = SearchSortFilter.getSearchItemsPriceList(BODY);

        List<Integer> searchResultsListOnServerAZ = searchResultsListOnServer.stream().sorted().collect(Collectors.toList());
        List<Integer> searchResultsListOnServerZA = searchResultsListOnServer.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        assertEquals(searchResultsListAZ, searchResultsListOnServerAZ);
        assertEquals(searchResultsListZA, searchResultsListOnServerZA);
    }
}