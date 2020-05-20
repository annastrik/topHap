package org.tophap;

import org.junit.jupiter.api.Test;
import org.tophap.runner.SingleWebTest;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.MapPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HoverOverTest extends SingleWebTest {

    @Test
    void hoverOverTest() throws InterruptedException {

        HomePage homePage = new HomePage(getDriver());

        MapPage mapPage = homePage.tryForFreeStart();

        mapPage.forEachButtonInAnalyticMenu(eachButtonsHoverOver -> assertTrue(eachButtonsHoverOver.isDisplayed()));
    }
}