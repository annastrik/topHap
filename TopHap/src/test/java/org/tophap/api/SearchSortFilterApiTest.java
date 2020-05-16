package org.tophap.api;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SearchSortFilterApiTest {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String obtainPrice(JSONObject item) {
        return item.getJSONObject("_source").getJSONObject("rets").getJSONObject("Facts").getString("ListAmount");
    }

    public static String obtainAddress(JSONObject item) {
        return item.getJSONObject("_source").getJSONObject("address").getString("UnparsedAddress");
    }

    public static List<Integer> obtainSortedPricesList(List<String> list) {
        return list.stream().map(x -> x.replace(".00", ""))
                .map(Integer::parseInt).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    public static List<String> obtainSortedAddressesList(List<String> list) {
        return list.stream().sorted().collect(Collectors.toList());
    }

    public static int forEachItemInApiResponse(String body, Consumer<JSONObject> acceptElement) throws IOException {

        int searchResultsCountOnServer;
        HttpPost request = new HttpPost("https://staging-api.tophap.com/properties/search");
        request.setEntity(new StringEntity(body));

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            JSONObject obj = new JSONObject(EntityUtils.toString(entity));
            JSONArray items = obj.getJSONArray("items");
            searchResultsCountOnServer = items.length();

            for (int i = 0; i < searchResultsCountOnServer; i++) {
                acceptElement.accept(items.getJSONObject(i));
            }
        }
        return searchResultsCountOnServer;
    }
}