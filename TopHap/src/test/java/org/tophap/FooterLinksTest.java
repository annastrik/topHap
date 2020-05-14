package org.tophap;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.tophap.runner.MultipleTest;
import pages.HomePage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FooterLinksTest extends MultipleTest {

    private HomePage homePage;

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    @Order(1)
    @Test
    void FooterLinksAreClickable() throws InterruptedException {

        homePage = new HomePage(getDriver());

        homePage.forEachFooterLink(footerLink -> {
            assertTrue(footerLink.isDisplayed());
            assertTrue(TestHelper.isClickable(getDriver(), footerLink));
        });
    }

    @Order(2)
    @Test
    void FooterLinksHaveAPIResponse200() throws IOException {

        for (WebElement footerLink : homePage.footerLinks) {
            if ("Chat with Us".equals(footerLink.getText()) || "hello@tophap.com".equals(footerLink.getText())) {
                continue;
            } else {
                HttpGet request = new HttpGet(footerLink.getAttribute("href"));
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                }
            }
        }
    }
}