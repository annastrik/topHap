package org.tophap.model.api;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tophap.helpers.ApiHelper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchSortFilter {

    public static class SearchItem {

        private JSONObject item;

        public SearchItem(JSONObject item) {
            this.item = item;
        }

        public String getPrice() {
            return item.getJSONObject("_source").getJSONObject("rets").getJSONObject("Facts").getString("ListAmount");
        }

        public String getAddress() {
            return item.getJSONObject("_source").getJSONObject("address").getString("UnparsedAddress");
        }

        public String getZipCode() {
            return item.getJSONObject("_source").getJSONObject("address").getString("PostalCode");
        }
    }

    public static List<SearchItem> getSearchItemsList(String body, String url) throws IOException {

        List<SearchItem> result = new ArrayList<>();
        ApiHelper.doHttpRequest(url, body, response-> {
            JSONArray items = null;
            try {
                items = new JSONObject(EntityUtils.toString(response.getEntity())).getJSONArray("items");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < items.length(); i++) {
                result.add(new SearchItem(items.getJSONObject(i)));
            }
        });

        return result;
    }

    public static Set<String> getSearchItemsSet(String body, String url) throws IOException {

    Set<String> result = new HashSet<>();
        ApiHelper.doHttpRequest(url, body, response-> {
            JSONArray items = null;
            try {
                items = new JSONObject(EntityUtils.toString(response.getEntity())).getJSONArray("items");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < items.length(); i++) {
                result.add(new SearchItem(items.getJSONObject(i)).getZipCode());
            }
        });

        return result;
    }

    public static List<Integer> getSearchItemsPriceList(String body, String url) throws IOException {
        return getSearchItemsList(body, url).stream()
                .map(SearchSortFilter.SearchItem::getPrice)
                .map(x -> x.replace(".00", ""))
                .map(Integer::parseInt)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}