package org.tophap;

import org.junit.jupiter.api.Test;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.runner.SingleApiTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchSoldPropertiesApiTest extends SingleApiTest {

    private static final String STATUS = "Sold";

    @Test
    void allItemsReturnedFromServerHaveSoldStatus() throws IOException {

        List<String> itemsWithStatusNotSold = SearchSortFilter.getSearchItemsList(SearchSortFilter.getSearchBodyByStatus(STATUS)).stream()
                .map(SearchSortFilter.SearchItem::getStatus)
                .filter(x -> !STATUS.equals(x))
                .collect(Collectors.toList());
        assertEquals(0, itemsWithStatusNotSold.size());
    }
}
