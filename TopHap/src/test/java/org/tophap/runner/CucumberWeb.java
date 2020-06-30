package org.tophap.runner;

import io.cucumber.java.After;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openqa.selenium.WebDriver;

public class CucumberWeb {

    private static WebImpl webImpl;
    private static ApiImpl apiImpl = new ApiImpl();

    public static WebDriver getDriver() {
        if (webImpl == null) {
            webImpl = new WebImpl();
            webImpl.beforeTest();
        }

        return webImpl.getDriver();
    }

    public static CloseableHttpClient getHttpClient() {
        return apiImpl.getHttpClient();
    }

    @After
    public static void callAfterTest() {
        if (webImpl != null) {
            webImpl.afterTest();
            webImpl = null;
        }
    }
}
