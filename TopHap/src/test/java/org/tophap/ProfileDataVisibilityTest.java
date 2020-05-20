package org.tophap;

import org.tophap.helpers.UserHelper;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.LoginPage;
import org.tophap.model.pages.ProfilePage;
import org.junit.jupiter.api.Test;
import org.tophap.runner.SingleWebTest;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileDataVisibilityTest extends SingleWebTest {

    @Test
    void checkProfileData() {

        // Open sign in form from the Home page and login
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.openLogin();
        loginPage.login(UserHelper.EMAIL, UserHelper.PASSWORD);

        // Close email confirmation failure PopUp window
        homePage.closeEmailConfirmationFailureMsg();

        // Go to My Account page
        ProfilePage profilePage = loginPage.openUserProfile();

        // Verify that all profile info fields are not empty
        assertFalse(profilePage.getName().isEmpty());
        assertFalse(profilePage.getUserName().isEmpty());
        assertFalse(profilePage.getPhoneNumber().isEmpty());
        assertFalse(profilePage.getEmail().isEmpty());

        // Verify that all profile info fields are displayed
        assertTrue(profilePage.nameField.isDisplayed());
        assertTrue(profilePage.usernameField.isDisplayed());
        assertTrue(profilePage.phoneNumberField.isDisplayed());
        assertTrue(profilePage.emailField.isDisplayed());
        assertTrue(profilePage.avatarPhoto.isDisplayed());
    }
}
