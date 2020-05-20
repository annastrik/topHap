package org.tophap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.tophap.helpers.UserHelper;
import org.tophap.runner.MultipleWebTest;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.LoginPage;
import org.tophap.model.pages.SignUpPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginFunctionalityTest extends MultipleWebTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeEach
    void createPageObjects() {

        homePage = new HomePage(getDriver());
    }

    @Order(1)
    @Test
    void verifyLoginPage() {

        loginPage = homePage.openLogin();

        assertEquals("Sign In", loginPage.title.getText());
    }

    @Order(2)
    @Test
    void redirectToSignUpPage(){

        loginPage = homePage.openLogin();

        assertTrue(loginPage.signUpButton.isEnabled());
        loginPage.signUpButton.click();
        SignUpPage signUpPage = new SignUpPage(getDriver());
        assertEquals("Sign Up for Free", signUpPage.title.getText());

        homePage.closeModalWindowButton.click();
    }

    @Order(3)
    @Test
    void redirectToForgotPasswordForm(){

        loginPage = homePage.openLogin();

        assertTrue(loginPage.forgotPasswordButton.isEnabled());
        assertEquals("Forgot Password?", loginPage.forgotPasswordButton.getText());

        loginPage.forgotPasswordButton.click();

        assertEquals("Forgot Password", loginPage.titleForgotPasswordForm.getText());

        homePage.closeModalWindowButton.click();
        }

    @Order(4)
    @Test
    void socialSignInButtonsEnabled() {

        loginPage = homePage.openLogin();

        assertTrue(loginPage.googleButton.isEnabled());
        assertTrue(loginPage.facebookButton.isEnabled());
    }

    @Order(5)
    @Test
    void loginWithValidCredentials() {

        loginPage = homePage.openLogin();

        loginPage.login(UserHelper.EMAIL, UserHelper.PASSWORD);
        loginPage.closeEmailConfirmationFailureMsg();
        loginPage.openUserProfile();
    }

    @Order(6)
    @Test
    void logout() { loginPage.logout();}
}

