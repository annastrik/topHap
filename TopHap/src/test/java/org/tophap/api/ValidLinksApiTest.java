package org.tophap.api;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.function.IntConsumer;

public class ValidLinksApiTest {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void forEachLinkAPIResponse(IntConsumer acceptElement, String Url) throws IOException {
        HttpGet request = new HttpGet(Url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            acceptElement.accept(response.getStatusLine().getStatusCode());
        }
    }
}

