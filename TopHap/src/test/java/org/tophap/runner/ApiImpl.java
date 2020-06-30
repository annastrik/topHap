package org.tophap.runner;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

class ApiImpl {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}