package org.tophap.model.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.tophap.model.pages.base.MainPage;

public class HomePage extends MainPage {

    public static final String URL = "https://next.tophap.com/";

    @FindBy(className = "th-signup-button")
    public WebElement signUpMenuBtn;

    @FindBy(xpath = "//button[text()='Try for Free']")
    public WebElement tryForFreeBtn;

    @FindBy(xpath = "//a[@class='th-signin-button']")
    public WebElement signInMenuBtn;

    public HomePage(WebDriver driver) {
        super(driver);
        driver.get(URL);
        driver.manage().window().maximize();
    }

    public SignUpPage openSignUp() {
        signUpMenuBtn.click();
        return new SignUpPage(getDriver());
    }

    public MapPage tryForFreeStart() {
        tryForFreeBtn.click();
        super.closeWelcome();
        return new MapPage(getDriver());
    }

    public LoginPage openLogin() {
        signInMenuBtn.click();

        return new LoginPage(getDriver());
    }
}