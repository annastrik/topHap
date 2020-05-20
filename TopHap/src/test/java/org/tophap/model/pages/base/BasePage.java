package org.tophap.model.pages.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;


public abstract class BasePage {

    private WebDriver driver;

    private WebDriverWait wait10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait10 = new WebDriverWait(driver, 10);

        PageFactory.initElements(driver, this);
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected WebDriverWait getWait10() {
        return wait10;
    }
}
