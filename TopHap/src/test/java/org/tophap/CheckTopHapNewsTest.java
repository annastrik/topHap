package org.tophap;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.tophap.helpers.UserHelper;
import org.tophap.runner.MultipleWebTest;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.LoginPage;
import org.tophap.model.pages.ProfilePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckTopHapNewsTest extends MultipleWebTest {

    private ProfilePage profilePage;

    @Order(1)
    @Test
    void login() {

        HomePage homePage = new HomePage(getDriver());

        LoginPage loginPage = homePage.openLogin();
        loginPage.login(UserHelper.EMAIL, UserHelper.PASSWORD);
        loginPage.closeEmailConfirmationFailureMsg();

        profilePage = loginPage.openUserProfile();
    }

    @Order(2)
    @Test
    void checkTopHapNewsTest(){

        String classNameBefore = getDriver().findElement(By.xpath("//span[@class='MuiIconButton-label']//parent::span")).getAttribute("className");
        boolean before = classNameBefore.contains("Mui-checked");

        profilePage.checkboxTopHapNews.click();
        profilePage.submitBtn.click();

        String classNameAfter = getDriver().findElement(By.xpath("//span[@class='MuiIconButton-label']//parent::span")).getAttribute("className");
        boolean after = classNameAfter.contains("Mui-checked");

        assertTrue(before ^ after);
    }

    @Order(3)
    @Test
    void logout() {
        profilePage.logout();
    }
}