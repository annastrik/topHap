package org.tophap.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.tophap.helpers.UserHelper;
import org.tophap.model.pages.HomePage;
import org.tophap.model.pages.LoginPage;
import org.tophap.model.pages.MapPage;
import org.tophap.runner.CucumberWeb;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tophap.model.api.SearchSortFilter.*;

public class SearchResult {

    private HomePage homePage;
    private MapPage mapPage;

    private void search(String str) throws InterruptedException {
        mapPage.submitSearch(str);
        mapPage.applyActivePropertyStatusFilter();
    }

    @Given("Go to tophap.com and login")
    public void goToTopHapAndLogin() {
        homePage = new HomePage(CucumberWeb.getDriver());
        LoginPage loginPage = homePage.openLogin();
        loginPage.login(UserHelper.EMAIL, UserHelper.PASSWORD);
        homePage.closeEmailConfirmationFailureMsg();
    }

    @Then("Go to Map page")
    public void goToMapPage() {
        mapPage = homePage.tryForFreeStart();
    }

    @When("City search {string}")
    public void citySearch(String cityName) throws InterruptedException {
        search(cityName);
    }

    @Then("First result's city is {string}")
    public void firstResultsCity(String cityName) {
        List<SearchItem> searchItemList = mapPage.getListItemFromSearchResult();
        assertEquals(cityName.toUpperCase(), searchItemList.get(0).getCity());
    }

    @When("Zip code search {string}")
    public void zipCodeSearch(String zipCode) throws InterruptedException {
        citySearch(zipCode);
    }

    @Then("First result's zip code is {string}")
    public void firstResultsZipCode(String zipCode) {
        List<SearchItem> searchItemList = mapPage.getListItemFromSearchResult();
        assertEquals(zipCode, searchItemList.get(0).getZipCode());
    }

}
