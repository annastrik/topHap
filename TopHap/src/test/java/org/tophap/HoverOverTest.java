package org.tophap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tophap.runner.SingleWebTest;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.MapPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HoverOverTest extends SingleWebTest {

    // Todo There is a bug, tooltips do not show (THBUGS-30)
    @Disabled
    @Test
    void hoverOverTest() throws InterruptedException {

        HomePage homePage = new HomePage(getDriver());

        MapPage mapPage = homePage.tryForFreeStart();

        mapPage.forEachButtonInAnalyticMenu(eachButtonsHoverOver -> assertTrue(eachButtonsHoverOver.isDisplayed()));
    }
}