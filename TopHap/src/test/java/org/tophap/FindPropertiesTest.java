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
}