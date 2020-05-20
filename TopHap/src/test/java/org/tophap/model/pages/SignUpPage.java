package org.tophap.model.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.tophap.model.pages.base.MainPage;

import java.util.List;

public class SignUpPage extends MainPage {

    @FindBy(xpath = "//div[@role='dialog']//h1")
    public WebElement title;

    @FindBy(xpath = "//input[@placeholder='Name']")
    public WebElement nameField;

    @FindBy(xpath = "//input[@placeholder='Email']")
    public WebElement emailField;

    @FindBy(xpath = "//input[@placeholder='Password']")
    public WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement submitBtn;

    @FindBy(className = "th-authentication-modal")
    public WebElement authentication;

    @FindBy(xpath = "//div[@class='jsx-2476985185 th-authentication']")
    WebElement emailConfirmation;

    @FindBy(xpath = "//h1[@class='mt-5 mb-4 th-signup-success-title']")
    public WebElement welcomeHeading;

    @FindBy(xpath = "//button[text()='RESEND EMAIL']")
    public WebElement welcomeResendEmailBtn;

    @FindBy(xpath = "//button[text()='OK']")
    public WebElement welcomeOkBtn;

    @FindBys({
            @FindBy(xpath = "//input[@placeholder='Name' and @required]")
    })
    public List<WebElement> nameMandatoryIndicator;

    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    public String getSignUpWindowHeading () {
        return title.getText();
    }

    public String emailConfirmationText() { return emailConfirmation.getText(); }

    public void signUpAttemptNoName (String email, String password) {
        this.emailField.sendKeys(email);
        this.passwordField.sendKeys(password);
        this.submitBtn.click();
    }

    public void signUp( String name, String email, String password ){
        nameField.sendKeys(name);
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        submitBtn.click();
    }

}
