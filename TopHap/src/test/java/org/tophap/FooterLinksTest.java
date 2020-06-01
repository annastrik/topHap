package org.tophap;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.tophap.helpers.ApiHelper;
import org.tophap.helpers.TestHelper;
import org.tophap.runner.MultipleWebTest;
import org.tophap.model.pages.HomePage;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FooterLinksTest extends MultipleWebTest {

    @Order(1)
    @Test
    void FooterLinksAreClickable() {

        HomePage homePage = new HomePage(getDriver());

        homePage.forEachFooterLink(footerLink -> {
            assertTrue(footerLink.isDisplayed());
            assertTrue(TestHelper.isClickable(getDriver(), footerLink));
        });
    }
}