package org.tophap.model.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.tophap.helpers.TestHelper;
import org.tophap.model.pages.base.MainPage;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MapPage extends MainPage {

    public static final By REGION_LOCATOR = By.cssSelector(".th-region");
    public static final By ADDRESS_LOCATOR = By.cssSelector(".th-address");
    public static final By PRICE_LOCATOR = By.cssSelector(".th-price");
    public static final By STATUS_LOCATOR = By.cssSelector(".th-status-label");

    @FindBy(id = "th-geo-input")
    public WebElement searchInputField;

    @FindBys({
            @FindBy(className = "th-clear-button")
    })
    public List<WebElement> clearSearchFilterBtns;

    @FindBy(className = "th-search-button")
    public WebElement searchBtn;

    @FindBy(xpath = "//aside[contains(@class,'th-sider')]")
    public WebElement searchResultsMenu;

    @FindBy(className = "th-trigger")
    public WebElement sortMenu;

    @FindBy(xpath = "//button[text()='A-Z']")
    public WebElement sortAZBtn;

    @FindBy(xpath = "//button[text()='Z-A']")
    public WebElement sortZABtn;

    @FindBy(xpath = "//button[text()='Price']")
    public WebElement selectPriceSorting;

    @FindBy(xpath = "//div[text()='Property Status']")
    public WebElement propertyStatusFilterMenu;

    @FindBy(xpath = "//label//span[text()='Active']")
    public WebElement activePropertyFilter;

    @FindBy(xpath = "//div[@class='jsx-1707507361 th-popover th-popover--expanded th-status-option']")
    public WebElement filterDropDownMenu;

    @FindBy(className = "th-more-container")
    public WebElement moreContainerBtn;

    private static final Map<String, String> buttonsWithHoverOvers = new HashMap<>();

    static {
        buttonsWithHoverOvers.put("Listings", "Properties");
        buttonsWithHoverOvers.put("Value Estimates", "Estimated Property Values");
        buttonsWithHoverOvers.put("$/ft² Estimates", "Estimated Price per Square Foot");
        buttonsWithHoverOvers.put("Living Area", "Property Living Area (square feet)");
        buttonsWithHoverOvers.put("Bedroom Count", "Property Number of Bedrooms");
        buttonsWithHoverOvers.put("Bathroom Count", "Property Number of Bathrooms");
        buttonsWithHoverOvers.put("Lot Size", "Property Lot Size (acres)");
        buttonsWithHoverOvers.put("Age", "Property Age (years)");
        buttonsWithHoverOvers.put("Ownership Time", "Current Ownership Time (days)");
        buttonsWithHoverOvers.put("DOM", "Days on market");
        buttonsWithHoverOvers.put("List vs Sold", "List Price to Sell Price Ratio (%)");
        buttonsWithHoverOvers.put("Walkability", "National Walkability Index");
        buttonsWithHoverOvers.put("Elevation", "Elevation above sea level");
        buttonsWithHoverOvers.put("Permits", "Permits");
    }

    public MapPage(WebDriver driver) {
        super(driver);
    }

    public void clearOldSearchAndFilterRecords() {
        if (this.clearSearchFilterBtns.size() > 0) {
            for (WebElement clearSearchFilterBtn : clearSearchFilterBtns) {
                clearSearchFilterBtn.click();
            }
        }
    }

    public void submitSearch(String searchCriteria) throws InterruptedException {
        this.searchInputField.sendKeys(searchCriteria);
        Thread.sleep(2000);
        this.searchBtn.click();
    }

    public void submitSearchApplySortingAndFiltersAZ(String zipCode) throws InterruptedException {
        submitSearchApplySortingAndFilters(this.sortAZBtn, zipCode);
    }

    public void submitSearchApplySortingAndFiltersZA(String zipCode) throws InterruptedException {
        submitSearchApplySortingAndFilters(this.sortZABtn, zipCode);
    }

    private void submitSearchApplySortingAndFilters(WebElement orderAtoZorZtoA, String zipCode) throws InterruptedException {
        this.clearOldSearchAndFilterRecords();
        this.submitSearch(zipCode);
        getWait10().until(ExpectedConditions.visibilityOf(this.searchResultsMenu));
        this.sortMenu.click();
        orderAtoZorZtoA.click();
        this.selectPriceSorting.click();
        this.propertyStatusFilterMenu.click();
        getWait10().until(ExpectedConditions.visibilityOf(this.filterDropDownMenu));
        this.activePropertyFilter.click();
    }

    private void applyPropertyStatusFilter(WebElement filterType) throws InterruptedException {
        this.propertyStatusFilterMenu.click();
        getWait10().until(ExpectedConditions.visibilityOf(this.filterDropDownMenu));
        filterType.click();
    }

    public void applyActivePropertyStatusFilter() throws InterruptedException {
        applyPropertyStatusFilter(this.activePropertyFilter);
    }

    private static final By SEARCH_ITEM_LOCATOR = By.cssSelector(".th-item-wrapper");

    public int forEachItemInSearchResult(Consumer<WebElement> acceptElement) {
        List<WebElement> resultSearchResultList = new ArrayList<>();
        int index = 0;

        List<WebElement> currentSearchResultList = getDriver().findElements(SEARCH_ITEM_LOCATOR);
        while (currentSearchResultList.size() > 0) {
            WebElement currentElement = currentSearchResultList.get(index);
            acceptElement.accept(currentElement);

            if (index == currentSearchResultList.size() - 1) {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", currentElement);
                getWait10().until(TestHelper.movingIsFinished(currentElement));

                resultSearchResultList.addAll(currentSearchResultList);
                List<WebElement> newSearchResultList = getDriver().findElements(SEARCH_ITEM_LOCATOR);
                newSearchResultList.removeAll(resultSearchResultList);
                currentSearchResultList = newSearchResultList;
                index = -1;
            }
            index++;
        }

        return resultSearchResultList.size();
    }

    public void forEachButtonInAnalyticMenu(Consumer<WebElement> eachButtonsHoverOver) throws InterruptedException {
        for (Map.Entry<String, String> entry : buttonsWithHoverOvers.entrySet()) {
            TestHelper.moveToHiddenElement(getDriver(), getDriver().findElement(By.xpath(
                    String.format("//span[text()='%s']", entry.getKey()))), this.moreContainerBtn);
            eachButtonsHoverOver.accept(getDriver().findElement(By.xpath(String.format("//div[text()='%s']", entry.getValue()))));
        }
    }

    public List<String> getSortedAddressesList(List<String> list) {
        return list.stream().map(x -> x.replace("Apt", ""))
                .map(x -> x.replace("Unit", "")).map(String::toUpperCase).sorted()
                .collect(Collectors.toList());
    }

    public static WebElement getRegionFromSearchItemResult(WebElement searchItem) {
        return searchItem.findElement(REGION_LOCATOR);
    }

    public static int getPriceFromSearchItemResult(WebElement searchItem) {
        return Integer.parseInt(searchItem.findElement(PRICE_LOCATOR).getText().replaceAll("[$,]", ""));
    }
}