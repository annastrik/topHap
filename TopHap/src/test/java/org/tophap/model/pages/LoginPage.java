package org.tophap.model.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.tophap.model.pages.base.MainPage;

public class LoginPage extends MainPage {

    @FindBy(xpath = "//h1[@class='th-form-title']")
    public WebElement title;

    @FindBy(xpath = "//button[@class='MuiButtonBase-root th-button th-ghost']")
    public WebElement signUpButton;

    @FindBy(xpath = "//a[@class='th-link ml-3']")
    public WebElement forgotPasswordButton;

    @FindBy(xpath = "//h1[@class='th-form-title']")
    public WebElement titleForgotPasswordForm;

    @FindBy(xpath = "//span[contains(text(),'Sign in with Google')]")
    public WebElement googleButton;

    @FindBy(xpath = "//span[contains(text(),'Continue with Facebook')]")
    public WebElement facebookButton;

    @FindBy(xpath = "//input[@placeholder='E-mail']")
    public WebElement emailField;

    @FindBy(xpath = "//input[@placeholder='Password']")
    public WebElement passwordField;

    @FindBy(xpath = "//button[@class='MuiButtonBase-root th-button']")
    public WebElement submitBtn;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String email, String password) {

        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        submitBtn.click();
    }
}
