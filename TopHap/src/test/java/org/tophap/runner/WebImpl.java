package org.tophap.runner;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

class WebImpl {

    public static final String HUB_URL = "http://localhost:4444/wd/hub";

    private static boolean remoteWebDriver = false;
    static {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(HUB_URL + "/status");
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                remoteWebDriver = response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
            } catch (HttpHostConnectException ignored) {}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isRemoteWebDriver() {
        return remoteWebDriver;
    }

    private WebDriver driver;

    void beforeTest() {

        if (isRemoteWebDriver()) {
            try {
                this.driver = new RemoteWebDriver(new URL(HUB_URL), DesiredCapabilities.chrome());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.driver = new ChromeDriver();
        }

        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    void afterTest() {
        driver.quit();
    }

    WebDriver getDriver() {
        return driver;
    }
}
