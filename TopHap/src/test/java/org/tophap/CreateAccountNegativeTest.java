package org.tophap;

import org.tophap.helpers.UserHelper;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.SignUpPage;
import org.junit.jupiter.api.Test;
import org.tophap.runner.SingleWebTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateAccountNegativeTest extends SingleWebTest {

    @Test
    void attemptToCreateAccountWithFailedData() throws InterruptedException {

        HomePage homePage = new HomePage(getDriver());
        SignUpPage signUpPage = homePage.openSignUp();

        assertEquals("Sign Up for Free", signUpPage.getSignUpWindowHeading());

        signUpPage.signUpAttemptNoName("qualityA2019+TA" + Math.round(Math.random()*1000) + "@gmail.com", UserHelper.PASSWORD);

        // verify the Name field is mandatory and that you are left of the Sign up form
        assertTrue(signUpPage.nameMandatoryIndicator.size() == 1);
        assertEquals("Sign Up for Free", signUpPage.getSignUpWindowHeading());
    }
}