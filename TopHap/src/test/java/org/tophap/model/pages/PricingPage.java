package org.tophap.model.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.tophap.model.pages.base.MainPage;

import java.util.List;
import java.util.function.BiConsumer;

public class PricingPage extends MainPage {

    @FindBys({
            @FindBy(xpath = "//div[contains(@class,'th-plan-info')]")
    })
    private List<WebElement> plansList;

    @FindBy (xpath = "//h1[@class='jsx-948257472 th-page-title']")
    public WebElement pageTitle;

    public PricingPage(WebDriver driver) {
        super(driver);
    }

    public void forEachByPlans(BiConsumer<WebElement, WebElement> test) {
        for (WebElement element : plansList) {
            WebElement plan = element.findElement(By.className("th-plan-role"));
            WebElement button = element.findElement(By.tagName("button"));

            test.accept(plan, button);
        }
    }
}