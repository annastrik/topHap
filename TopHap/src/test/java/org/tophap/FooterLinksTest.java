package org.tophap;

import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.api.ValidLinksApiTest;
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

        ValidLinksApiTest.forEachFooterLinkAPIResponse(APIResponse -> assertEquals(HttpStatus.SC_OK, APIResponse), getDriver());
    }
}