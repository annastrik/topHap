package pages.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.tophap.TestHelper;
import pages.ProfilePage;

import java.util.List;
import java.util.function.Consumer;

public abstract class MainPage extends BasePage {

    @FindBy(xpath = "//div[@class='jsx-3275066862 th-menu-item th-avatar-wrapper']")
    public WebElement avatarMenuButton;

    @FindBy(xpath = "//li[text()='Sign Out']")
    public WebElement signOutButton;

    @FindBy(linkText = "Account")
    public WebElement accountMenuButton;

    @FindBys({
            @FindBy(xpath = "//iframe[@title='Intercom Live Chat']")
    })
    public List<WebElement> welcomeWindow;

    @FindBy(xpath = "//span[@aria-label='Close']")
    public WebElement closeFrameBtn;

    @FindBy(xpath = "//button[@class='MuiButtonBase-root th-button th-close-button']")
    public WebElement closeBtn;

    @FindBy(className = "th-close-button")
    public WebElement closeModalWindowButton;

    @FindBy(xpath = "//a[@class='th-logo']")
    public WebElement siteLogo;

    @FindBys({
            @FindBy(css = ".th-menu-item")
    })
    public List<WebElement> mainMenu;

    @FindBy(xpath = "//div[text()='Find Properties']")
    public WebElement findPropertiesMenuItem;

    @FindBy(xpath = "//li[text()='All Active Listings']")
    public WebElement activePropertiesSubMenuItem;

    @FindBy(xpath = "//li[text()='Recently Sold']")
    public WebElement recentlySoldSubMenuItem;

    @FindBy(xpath = "//li[text()='New Construction']")
    public WebElement newConstructionSubMenuItem;

    @FindBys({
            @FindBy(css = ".th-link")
    })
    public List<WebElement> footerLinks;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void logout() {
        TestHelper.moveToElement(getDriver(), avatarMenuButton);
        signOutButton.click();
    }

    public ProfilePage openUserProfile() {
        TestHelper.moveToElement(getDriver(), avatarMenuButton);
        accountMenuButton.click();

        return new ProfilePage(getDriver());
    }

    public void closeWelcome() {
        if (welcomeWindow.size() > 0) {
            getDriver().switchTo().frame(welcomeWindow.get(0));
            closeFrameBtn.click();
            getDriver().switchTo().defaultContent();
        }
    }

    public void closeEmailConfirmationFailureMsg() {
        getWait10().until(TestHelper.movingIsFinished(
                By.xpath("//div[@class='Toastify__toast-container Toastify__toast-container--top-right th-notification-container']")));
        closeBtn.click();
    }

    public MainPage goToHome() {
        siteLogo.click();

        return this;
    }

    public void openFindPropertiesMenu() {
        TestHelper.moveToElement(getDriver(), findPropertiesMenuItem);
    }

    public void forEachFooterLink(Consumer<WebElement> acceptElement) {
        for (WebElement footerLink : this.footerLinks) {
            acceptElement.accept(footerLink);
        }
    }
}