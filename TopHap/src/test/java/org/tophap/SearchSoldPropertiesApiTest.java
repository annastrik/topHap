package org.tophap;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.runner.MultipleApiTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchSoldPropertiesApiTest extends MultipleApiTest {

    private static final String STATUS = "Sold";

    @Order(1)
    @Test
    void soldStatusReturned_HttpClientInterface() throws IOException {

        List<String> itemsWithStatusNotSold = SearchSortFilter.getSearchItemsList(SearchSortFilter.getSearchBodyByStatus(STATUS)).stream()
                .map(SearchSortFilter.SearchItem::getStatus)
                .filter(x -> !STATUS.equals(x))
                .collect(Collectors.toList());
        assertEquals(0, itemsWithStatusNotSold.size());
    }

    @Order(2)
    @Test
    void soldStatusReturned_RestAssuredInterface() throws IOException {

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(SearchSortFilter.getSearchBodyByStatus(STATUS))
                .when()
                .post("https://staging-api.tophap.com/properties/search")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("items._source.rets.Facts.TophapStatus", Matchers.everyItem(Matchers.equalTo(STATUS)));
    }
}
