package org.tophap.model.api;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FirstTest {

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    @Test
    void test() throws IOException {

        String body = "{\"size\":30,\"sort\":[{\"option\":\"status_timestamp\",\"dir\":\"desc\"}],\"filters\":{\"bounds\":[[-122.52593172019408,37.647025377860245],[-122.42688917980605,37.70980939999299]],\"zones\":[\"000394015\"],\"metricsFilter\":{\"baths\":{},\"beds\":{},\"garage_spaces\":{},\"living_area\":{},\"lot_acres\":{},\"ownership_days\":{},\"period\":{},\"price\":{},\"price_sqft\":{},\"property_type\":{\"values\":[]},\"rental\":false,\"status\":{\"values\":[],\"close_date\":{\"min\":\"now-1M/d\"}},\"stories\":{},\"year_built\":{}}}}";

        HttpPost request = new HttpPost("https://staging-api.tophap.com/properties/search");
        request.setEntity(new StringEntity(body));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            assertNotNull(entity, "response is null");

            //System.out.println(EntityUtils.toString(entity));

            JSONObject obj = new JSONObject(EntityUtils.toString(entity));

            JSONArray items = obj.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                String price = items.getJSONObject(i).getJSONObject("_source").getJSONObject("rets").getJSONObject("Facts").getString("ListAmount");
                System.out.println(price);
            }
        }
    }
}
