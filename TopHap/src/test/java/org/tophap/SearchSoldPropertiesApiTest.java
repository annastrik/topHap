package org.tophap;

import org.junit.jupiter.api.Test;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.runner.SingleApiTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchSoldPropertiesApiTest extends SingleApiTest {

    private static final String BODY = "{\"size\":30,\"sort\":[{\"option\":\"status_timestamp\",\"dir\":\"desc\"}],\"filters\":{\"bounds\":[[-123.43697048056796,37.34582735413538],[-122.0022079172666,38.0975091572424]],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"close_date\":{\"min\":\"now-1y/d\"},\"values\":[\"Sold\"]},\"stories\":{},\"year_built\":{}}}}";

    @Test
    void allItemsReturnedFromServerHaveSoldStatus() throws IOException {

        List<String> itemsWithStatusNotSold = SearchSortFilter.getSearchItemsList(BODY).stream()
                .map(SearchSortFilter.SearchItem::getStatus)
                .filter(x -> !"Sold".equals(x))
                .collect(Collectors.toList());
        assertEquals(0, itemsWithStatusNotSold.size());
    }
}
