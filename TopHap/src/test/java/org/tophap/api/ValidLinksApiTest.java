package org.tophap.api;

import org.openqa.selenium.WebDriver;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;
import java.util.function.IntConsumer;

public class ValidLinksApiTest {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void forEachFooterLinkAPIResponse(IntConsumer acceptElement, WebDriver driver) throws IOException {
        List<WebElement> footerLinks = driver.findElements(By.cssSelector(".th-link"));
        for (WebElement footerLink : footerLinks) {
            if ("Chat with Us".equals(footerLink.getText()) || "hello@tophap.com".equals(footerLink.getText())) {
                continue;
            } else {
                HttpGet request = new HttpGet(footerLink.getAttribute("href"));
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    acceptElement.accept(response.getStatusLine().getStatusCode());
                }
            }
        }
    }
}