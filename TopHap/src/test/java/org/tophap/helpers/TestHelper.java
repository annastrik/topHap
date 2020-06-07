package org.tophap.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Point;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestHelper {

    private static class MovingExpectedCondition implements ExpectedCondition<WebElement> {

        private By locator;
        private WebElement element = null;
        private Point location = null;

        public MovingExpectedCondition(WebElement element) {
            this.element = element;
        }

        public MovingExpectedCondition(By locator) {
            this.locator = locator;
        }

        @Override
        public WebElement apply(WebDriver driver) {
            if (element == null) {
                try {
                    element = driver.findElement(locator);
                } catch (NoSuchElementException e) {
                    return null;
                }
            }

            if (element.isDisplayed()) {
                Point location = element.getLocation();
                if (location.equals(this.location)) {
                    return element;
                }
                this.location = location;
            }

            return null;
        }
    }

    public static ExpectedCondition<WebElement> movingIsFinished(final By locator) {
        return new MovingExpectedCondition(locator);
    }

    public static ExpectedCondition<WebElement> movingIsFinished(final WebElement element) {
        return new MovingExpectedCondition(element);
    }

    public static void moveToElement(WebDriver driver, WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
    }

    public static void moveToElementAndClick(WebDriver driver, WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).click().perform();
    }

    public static void moveToHiddenElement(WebDriver driver, WebElement analyticMenuBtn, WebElement moreContainerBtn) throws InterruptedException {
        Actions action = new Actions(driver);
        if (analyticMenuBtn.isDisplayed()) {
            action.moveToElement(analyticMenuBtn).perform();
        } else {
            action.moveToElement(moreContainerBtn).perform();
            TestHelper.moveToElement(driver,
                    new WebDriverWait(driver, 10).until(TestHelper.movingIsFinished(analyticMenuBtn)));
        }
    }

    public static boolean isClickable(WebDriver driver, WebElement element) {
        try {
            new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}