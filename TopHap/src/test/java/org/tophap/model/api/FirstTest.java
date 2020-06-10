package org.tophap.model.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.tophap.runner.MultipleApiTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FirstTest extends MultipleApiTest {

    private static final String BODY = "{\"size\":30,\"sort\":[{\"option\":\"status_timestamp\",\"dir\":\"desc\"}],\"filters\":{\"bounds\":[[-122.52593172019408,37.647025377860245],[-122.42688917980605,37.70980939999299]],\"zones\":[\"000394015\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}";

    private List<String> priceList = new ArrayList<>();

    @Order(1)
    @Test
    void test() throws IOException {

        HttpPost request = new HttpPost("https://staging-api.tophap.com/properties/search");
        request.setEntity(new StringEntity(BODY));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            assertNotNull(entity, "response is null");

            JSONObject obj = new JSONObject(EntityUtils.toString(entity));

            JSONArray items = obj.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                priceList.add(items.getJSONObject(i).getJSONObject("_source").getJSONObject("rets").getJSONObject("Facts").getString("ListAmount"));
            }
        }
    }

    private static Stream<Arguments> testAssured() {
        return Stream.of(
                Arguments.of(3, "three"),
                Arguments.of(2, "two"),
                Arguments.of(1, "one")
        );
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource
    void testAssured(Integer num, String name) throws IOException {

        System.out.print(num);
        System.out.print(" - ");
        System.out.println(name);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(BODY)
                .when()
                .post("https://staging-api.tophap.com/properties/search")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("items._source.rets.Facts.ListAmount", Matchers.hasItems(priceList.toArray()));
    }
}
