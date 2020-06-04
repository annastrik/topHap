package org.tophap.model.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.tophap.model.pages.base.MainPage;

public class ProfilePage extends MainPage {

    @FindBy(css = ".th-button.th-avatar-wrapper")
    public WebElement avatarPhoto;

    @FindBy(xpath = "//input[@placeholder='Name']")
    public WebElement nameField;

    @FindBy(xpath = "//input[@placeholder='Username']")
    public WebElement usernameField;

    @FindBy(xpath = "//input[@placeholder='Phone Number']")
    public WebElement phoneNumberField;

    @FindBy(xpath = "//input[@placeholder='Email']")
    public WebElement emailField;

    @FindBy(css = ".MuiIconButton-label")
    public WebElement checkboxTopHapNews;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement submitBtn;

    @FindBy(className = "th-close-account-button")
    WebElement deleteAccountBtn;

    @FindBy(xpath = "//div[contains(@class,'th-alert-modal')]//button[contains(@class,'th-ok-action')]")
    WebElement getDeleteAccOKBtn;

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    public void updateName(String name) {
        nameField.clear();
        nameField.sendKeys(name);
        submitBtn.click();
    }

    public void updateUserName(String userName) {
        usernameField.clear();
        usernameField.sendKeys(userName);
        submitBtn.click();
    }

    public void updatePhoneNumber(String phoneNumber) {
        phoneNumberField.clear();
        phoneNumberField.sendKeys(phoneNumber);
        submitBtn.click();
    }

    public void updateEmail(String email) {
        emailField.sendKeys(email);
        submitBtn.click();
    }

    public void updateCheckboxTopHapNews(){
        checkboxTopHapNews.click();
        submitBtn.click();
    }

    public String getName() {
        return nameField.getAttribute("value");
    }

    public String getUserName() {
        return usernameField.getAttribute("value");
    }

    public String getEmail() {
        return emailField.getAttribute("value");
    }

    public String getPhoneNumber() {
        return phoneNumberField.getAttribute("value");
    }

    public void deleteAccount() {
        deleteAccountBtn.click();
        getDeleteAccOKBtn.click();
    }
}