package org.tophap.runner;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

class ApiImpl {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}