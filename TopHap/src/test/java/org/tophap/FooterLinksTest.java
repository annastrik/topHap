package org.tophap;

import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.tophap.api.ValidLinksApiTest;
import org.tophap.runner.MultipleTest;
import pages.HomePage;

import java.io.IOException;
import java.util.List;

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

        final List<WebElement> footerLinks = getDriver().findElements(By.cssSelector(".th-link"));

        for (WebElement footerLink : footerLinks) {
            if (!("Chat with Us".equals(footerLink.getText()) || "hello@tophap.com".equals(footerLink.getText()))) {
                ValidLinksApiTest.forEachLinkAPIResponse(APIResponse ->
                        assertEquals(HttpStatus.SC_OK, APIResponse), footerLink.getAttribute("href"));
            }
        }
    }
}