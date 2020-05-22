package org.tophap;

import org.apache.http.HttpStatus;
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
        final String URL_RECENTLY_SOLD = "https://next.tophap.com/_next/static/chunks/1efffb724918bcf682686c208e9bde6bdffb13a7.85b5c6efe524a0e64b49.js";
        final String NEW_CONSTRUCTION = "https://next.tophap.com/_next/static/chunks/37455560100892d6452a891e5ff5d80c03e94cc3.243be9aa4f8f7be76535.js";

        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(URL_ACTIVE_PROPERTIES));
        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(URL_RECENTLY_SOLD));
        assertEquals(HttpStatus.SC_OK, ApiHelper.getHttpRequestStatus(NEW_CONSTRUCTION));
    }
}