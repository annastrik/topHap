package org.tophap;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.helpers.ApiHelper;
import org.tophap.helpers.TestHelper;
import org.tophap.runner.MultipleWebTest;
import org.tophap.model.pages.HomePage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindPropertiesTest extends MultipleWebTest {

    private HomePage homePage;

    @Order(1)
    @Test
    void FindPropertiesMainMenuItemIsOnThePage() throws InterruptedException {
        homePage = new HomePage(getDriver());
        assertTrue(homePage.mainMenu.contains(homePage.findPropertiesMenuItem));
        assertTrue(homePage.findPropertiesMenuItem.isDisplayed());
    }

    @Order(2)
    @Test
    void SubMenuItemsAreFunctional() throws InterruptedException {

        homePage.openFindPropertiesMenu();

        assertTrue(homePage.activePropertiesSubMenuItem.isDisplayed());
        assertTrue(homePage.recentlySoldSubMenuItem.isDisplayed());
        assertTrue(homePage.newConstructionSubMenuItem.isDisplayed());

        assertTrue(TestHelper.isClickable(getDriver(), homePage.activePropertiesSubMenuItem));
        assertTrue(TestHelper.isClickable(getDriver(), homePage.recentlySoldSubMenuItem));
        assertTrue(TestHelper.isClickable(getDriver(), homePage.newConstructionSubMenuItem));
    }

    @Disabled
    @Order(3)
    @Test
    void SubMenuLinksHaveAPIResponse200() throws IOException {

        final String URL_ACTIVE_PROPERTIES = "https://next.tophap.com/_next/static/chunks/71d48f31.3e7738b9463bcb7b11eb.js";
        final String URL_RECENTLY_SOLD = "https://next.tophap.com/_next/static/chunks/a0aeb5bc847f55bf5affb6d1f5886e2c395e19ab.d5494e58cac2c8a01382.js";
        final String NEW_CONSTRUCTION = "https://next.tophap.com/_next/static/chunks/38a4c9867534eec619f7da5c3419dd2db550cc40.905eebe5af49e9ade33c.js";

        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(URL_ACTIVE_PROPERTIES));
        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(URL_RECENTLY_SOLD));
        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(NEW_CONSTRUCTION));
    }
}