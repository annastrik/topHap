package org.tophap.runner;

import org.apache.http.impl.client.CloseableHttpClient;

public class SingleApiTest extends BaseTest {

    private ApiImpl apiImpl = new ApiImpl();

    public CloseableHttpClient getHttpClient() {
        return apiImpl.getHttpClient();
    }
}
