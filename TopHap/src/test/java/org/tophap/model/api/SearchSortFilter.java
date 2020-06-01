package org.tophap.model.api;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tophap.helpers.ApiHelper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchSortFilter {

    public static final String SEARCH_URL = "https://staging-api.tophap.com/properties/search";

    public static abstract class SearchItem {

        public abstract String getPrice();
        public abstract String getAddress();
        public abstract String getZipCode();
        public abstract String getCity();
        public abstract String getStatus();

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SearchItem)) {
                return false;
            }

            SearchItem that = (SearchItem)obj;
            return Objects.equals(that.getPrice(), this.getPrice())
                    && Objects.equals(that.getAddress(), this.getAddress())
                    && Objects.equals(that.getZipCode(), this.getZipCode())
                    && Objects.equals(that.getCity(), this.getCity())
                    && Objects.equals(that.getStatus(), this.getStatus());
        }
    }

    public static class SearchItemPOJO extends SearchItem {

        public String price;
        public String address;
        public String zipCode;
        public String city;
        public String status;

        public SearchItemPOJO(String price, String address, String zipCode, String city, String status) {
            this.price = price;
            this.address = address;
            this.zipCode = zipCode;
            this.city = city;
            this.status = status;
        }

        @Override
        public String getPrice() {
            return price;
        }

        @Override
        public String getAddress() {
            return address;
        }

        @Override
        public String getZipCode() {
            return zipCode;
        }

        @Override
        public String getCity() {
            return city;
        }

        @Override
        public String getStatus() {
            return status;
        }
    }

    public static class SearchItemJSON extends SearchItem {

        private JSONObject item;

        public SearchItemJSON(JSONObject item) {
            this.item = item;
        }

        @Override
        public String getPrice() {
            return item.getJSONObject("_source").getJSONObject("rets").getJSONObject("Facts").getString("ListAmount");
        }

        @Override
        public String getAddress() {
            return item.getJSONObject("_source").getJSONObject("address").getString("UnparsedAddress");
        }

        @Override
        public String getZipCode() {
            return item.getJSONObject("_source").getJSONObject("address").getString("PostalCode");
        }

        @Override
        public String getCity() {
            return item.getJSONObject("_source").getJSONObject("address").getString("City");
        }

        @Override
        public String getStatus() {
            return item.getJSONObject("_source").getJSONObject("rets").getJSONObject("Facts").getString("TophapStatus");
        }
    }

    public static List<SearchItem> getSearchItemsList(String body) throws IOException {

        List<SearchItem> result = new ArrayList<>();
        ApiHelper.doHttpRequest(SEARCH_URL, body,
                response -> {
                    JSONArray items = null;
                    try {
                        items = new JSONObject(EntityUtils.toString(response.getEntity())).getJSONArray("items");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < items.length(); i++) {
                        result.add(new SearchItemJSON(items.getJSONObject(i)));
                    }
                });

        return result;
    }

    public static List<Integer> getSearchItemsPriceList(String body) throws IOException {
        return getSearchItemsList(body).stream()
                .map(SearchItem::getPrice)
                .map(x -> x.replace(".00", ""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}