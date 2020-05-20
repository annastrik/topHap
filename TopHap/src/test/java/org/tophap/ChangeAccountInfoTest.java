package org.tophap;

import org.tophap.helpers.UserHelper;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.LoginPage;
import org.tophap.model.pages.ProfilePage;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.runner.MultipleWebTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeAccountInfoTest extends MultipleWebTest {

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
    void changeAccountPhoneTest() {

        final String PHONE_NUMBER = String.valueOf(System.currentTimeMillis()).substring(0, 10);

        profilePage.updatePhoneNumber(PHONE_NUMBER);
        profilePage = profilePage.goToHome().openUserProfile();

        assertEquals(PHONE_NUMBER, profilePage.getPhoneNumber());
    }

    @Order(3)
    @Test
    void changeAccountNameTest() {

        final String NAME = String.format("TestTest%s", Math.round(Math.random() * 100));

        profilePage.updateName(NAME);
        profilePage = profilePage.goToHome().openUserProfile();

        assertEquals(NAME, profilePage.getName());
    }

    @Order(4)
    @Test
    void changeUserNameTest() {

        final String USER_NAME = String.format("TestTest%s", Math.round(Math.random() * 100));

        profilePage.updateUserName(USER_NAME);
        profilePage = profilePage.goToHome().openUserProfile();

        assertEquals(USER_NAME, profilePage.getUserName());
    }

    @Order(5)
    @Test
    void changeEmailFailureTest() {

        final String NEW_EMAIL = "qualitya2019+ta2@gmail.com";

        profilePage.updateEmail(NEW_EMAIL);
        profilePage = profilePage.goToHome().openUserProfile();

        assertEquals(UserHelper.EMAIL, profilePage.getEmail());
    }

    @Order(6)
    @Test
    void logout() {
        profilePage.logout();
    }
}