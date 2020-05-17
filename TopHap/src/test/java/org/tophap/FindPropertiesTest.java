package org.tophap;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.api.ValidLinksApiTest;
import org.tophap.runner.MultipleTest;
import pages.HomePage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindPropertiesTest extends MultipleTest {

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

        final String URL_ACTIVE_PROPERTIES = "https://next.tophap.com/map/N4IgtiBcoMZQ2gWgIwCZUDoDsyCcBWADlz2OS1UIBoBmLbdANlXKJsbMYF0qQAvKIQxosABlQdcBXKnyjGvAA5RRvAEYr1AewRI0AFgyjydRqP2FU+xvpq16yUbnb594vGho896DDXyoNIT4uLZOJPq0QjQ0ooT6skQk+DSoXFwAvrwAzgBuUAAuAE4ArgCmGUA";
        final String URL_RECENTLY_SOLD = "https://next.tophap.com/map/N4IgZiBcoM4C4EM4FcZVAYwDYHsYFMB9AEyX3RAFsBLAOyhFpwHcBaARgE8B6YkAXwA0IAG4IsyfGkgBtEAGUcWPgF1+QkAAcK+WggBGWfH0hwATpOFYD+LAFE9h41HOWQGJTjMODRk2HECYTMEYmpUHycTV3xhD1wzBjCYTWtOAAUzagxyYNDw6XccZFo4ECsbLAYPErKNGBEXC3x+IA";
        final String NEW_CONSTRUCTION = "https://next.tophap.com/map/N4IgZiBcoJ4KYEMBOB9ARgVwJYBsAuUoAtlgHZQBMADAIwAcAvgwDQgAOhIcpCaOcAEyh4kGOKxy84OAKI8+g4aPEgAxgHsc6pHN78hkMAhwBnFUgQCsGE7oUGRY1hq1IoIKybaSYABSRYqnAgrBZWNu4aGKQEElI4kerRBCwgJgBuSmIMQA";

        ValidLinksApiTest.forEachLinkAPIResponse(APIResponse -> assertEquals(HttpStatus.SC_OK, APIResponse), URL_ACTIVE_PROPERTIES);
        ValidLinksApiTest.forEachLinkAPIResponse(APIResponse -> assertEquals(HttpStatus.SC_OK, APIResponse), URL_RECENTLY_SOLD);
        ValidLinksApiTest.forEachLinkAPIResponse(APIResponse -> assertEquals(HttpStatus.SC_OK, APIResponse), NEW_CONSTRUCTION);
    }
}