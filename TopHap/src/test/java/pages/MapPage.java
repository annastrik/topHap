package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.tophap.TestHelper;
import pages.base.MainPage;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MapPage extends MainPage {

    @FindBy(id = "th-geo-input")
    public WebElement searchInputField;

    @FindBys({
            @FindBy(className = "th-clear-button")
    })
    public List<WebElement> clearSeachBtns;

    @FindBys({
            @FindBy(className = "th-clear-icon")
    })
    public List<WebElement> clearFilterBtns;

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

    public void clearSearchField() {
        clearSeachBtns.get(0).click();
    }

    public void clearPropertyStatusFilter() {
        clearFilterBtns.get(0).click();
    }

    public void clearOldSearchAndFilterRecords() {
        if (this.clearSeachBtns.size() > 0) {
            this.clearSearchField();
        }
        if (this.clearFilterBtns.size() > 0) {
            this.clearPropertyStatusFilter();
        }
    }

    public void submitSearch(String postalCode) throws InterruptedException {
        this.searchInputField.sendKeys(postalCode);
        Thread.sleep(2000);
        this.searchBtn.click();
    }

    public void submitSearchApplySortingAndFiltersAZ() throws InterruptedException {
        submitSearchApplySortingAndFilters(this.sortAZBtn);
    }

    public void submitSearchApplySortingAndFiltersZA() throws InterruptedException {
        submitSearchApplySortingAndFilters(this.sortZABtn);
    }

    public static final String ZIP_TEST = "94523";

    private void submitSearchApplySortingAndFilters(WebElement orderAtoZorZtoA) throws InterruptedException {
        this.clearOldSearchAndFilterRecords();
        this.submitSearch(ZIP_TEST);
        getWait10().until(ExpectedConditions.visibilityOf(this.searchResultsMenu));
        this.sortMenu.click();
        orderAtoZorZtoA.click();
        this.selectPriceSorting.click();
        this.propertyStatusFilterMenu.click();
        getWait10().until(ExpectedConditions.visibilityOf(this.filterDropDownMenu));
        this.activePropertyFilter.click();
        //TODO refresh to be removed when bug is fixed: https://techacademeproject.atlassian.net/browse/THBUGS-29
        getDriver().navigate().refresh();
    }

    private void applyPropertyStatusFilter(WebElement filterType) throws InterruptedException {
        this.propertyStatusFilterMenu.click();
        getWait10().until(ExpectedConditions.visibilityOf(this.filterDropDownMenu));
        filterType.click();
        //TODO refresh to be removed when bug is fixed: https://techacademeproject.atlassian.net/browse/THBUGS-29
        getDriver().navigate().refresh();
    }

    public void applyActivePropertyStatusFilter() throws InterruptedException {
        //TODO refresh to be removed when bug is fixed: https://techacademeproject.atlassian.net/browse/THBUGS-29
        applyPropertyStatusFilter(this.activePropertyFilter);
        getDriver().navigate().refresh();
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

    public List<String> obtainSortedAddressesList(List<String> list) {
        return list.stream().map(x -> x.replace("Apt", ""))
                .map(x -> x.replace("Unit", "")).map(String::toUpperCase).sorted()
                .collect(Collectors.toList());
    }
}