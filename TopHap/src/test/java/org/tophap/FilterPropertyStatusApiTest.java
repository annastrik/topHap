package org.tophap;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tophap.model.api.SearchSortFilter;
import org.tophap.runner.MultipleApiTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.tophap.model.api.SearchSortFilter.SearchItem;

public class FilterPropertyStatusApiTest extends MultipleApiTest {

    @Order(1)
    @ParameterizedTest
    @ValueSource(strings = {"Sold", "Active", "Pending"})
    void soldStatusReturned_HttpClientInterface(String status) throws IOException {

        List<SearchItem> searchItemListOnServer = SearchSortFilter.getSearchItemsList(SearchSortFilter.getSearchBodyByStatus(status));

        assertEquals(searchItemListOnServer.size(),
                searchItemListOnServer.stream()
                        .map(SearchItem::getStatus)
                        .filter(status::equals)
                        .count());
    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(strings = {"Sold", "Active", "Pending"})
    void soldStatusReturned_RestAssuredInterface(String status) throws IOException {

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(SearchSortFilter.getSearchBodyByStatus(status))
                .when()
                .post("https://staging-api.tophap.com/properties/search")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("items._source.rets.Facts.TophapStatus", Matchers.everyItem(Matchers.equalTo(status)));
    }
}