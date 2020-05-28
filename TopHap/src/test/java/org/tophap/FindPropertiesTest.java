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

   
    @Order(3)
    @Test
    void SubMenuLinksHaveAPIResponse200() throws IOException {

        final String URL_ACTIVE_PROPERTIES = "https://next.tophap.com/_next/static/chunks/71d48f31.4324a6d0469f313c4195.js";
        final String URL_RECENTLY_SOLD = "https://next.tophap.com/_next/static/chunks/15e50bb8612abbbfca58efdc722e56c77ddb558d.e2ff4eb228a0f50bebf4.js";
        final String NEW_CONSTRUCTION = "https://next.tophap.com/_next/static/chunks/cde04e425d83429ec8aaab3c9e62cf903f5f92f8.01ba0e7b35c7e2c9cd95.js";

        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(URL_ACTIVE_PROPERTIES));
        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(URL_RECENTLY_SOLD));
        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(NEW_CONSTRUCTION));
    }
}