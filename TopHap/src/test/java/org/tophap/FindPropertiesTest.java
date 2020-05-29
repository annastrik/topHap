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

        final String URL_ACTIVE_PROPERTIES = "https://next.tophap.com/_next/static/chunks/71d48f31.2d01392e819407f15e14.js";
        final String URL_RECENTLY_SOLD = "https://next.tophap.com/_next/static/chunks/c8d486509f06b0bd3681680c4db5cf7d37dda2dc.ceace168607ab75caff5.js";
        final String NEW_CONSTRUCTION = "https://next.tophap.com/_next/static/chunks/7bed1f3d848597c86eaa279966486a7ed6de8d35.905eebe5af49e9ade33c.js";

        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(URL_ACTIVE_PROPERTIES));
        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(URL_RECENTLY_SOLD));
        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(NEW_CONSTRUCTION));
    }
}