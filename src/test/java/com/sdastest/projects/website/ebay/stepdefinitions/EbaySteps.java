package com.sdastest.projects.website.ebay.stepdefinitions;

import com.sdastest.keywords.WebUI;
import com.sdastest.projects.website.ebay.pages.EbayHomePage;
import com.sdastest.projects.website.ebay.pages.EbayProductPage;
import com.sdastest.projects.website.ebay.pages.EbaySearchResultsPage;
import com.sdastest.utils.LogUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class EbaySteps {

    private EbayHomePage ebayHomePage;
    private EbaySearchResultsPage searchResultsPage;
    private EbayProductPage productPage;

    public EbaySteps() {
        ebayHomePage = new EbayHomePage();
        searchResultsPage = new EbaySearchResultsPage();
        productPage = new EbayProductPage();
    }

    @Given("User navigates to {string}")
    public void userNavigatesToUrl(String url) {
        if (url.contains("ebay.com")) {
            ebayHomePage.navigateToEbay();
            ebayHomePage.verifyPageLoaded();
            LogUtils.info("Successfully navigated to eBay homepage");
        } else {
            WebUI.openWebsite(url);
            WebUI.waitForPageLoaded();
            LogUtils.info("Navigated to: " + url);
        }
    }

    @When("User enters {string} in the search bar")
    public void userEntersInTheSearchBar(String searchTerm) {
        ebayHomePage.searchForItem(searchTerm);
        LogUtils.info("Entered search term: " + searchTerm);
    }

    @When("User clicks the search button")
    public void userClicksTheSearchButton() {
        searchResultsPage = ebayHomePage.clickSearchButton();
        LogUtils.info("Clicked the search button");
    }

    @Then("User should see search results for {string}")
    public void userShouldSeeSearchResultsFor(String searchTerm) {
        searchResultsPage.verifySearchResultsDisplayed(searchTerm);
        LogUtils.info("Verified search results are displayed for: " + searchTerm);
    }

    @When("User clicks on the first book result")
    public void userClicksOnTheFirstBookResult() {
        productPage = searchResultsPage.clickFirstSearchResult();
        LogUtils.info("Clicked on the first search result");
    }

    @When("User switches to the new product tab")
    public void userSwitchesToTheNewProductTab() {
        WebUI.sleep(2);
        productPage.verifyProductPageLoaded();
        
        LogUtils.info("Switched to the new product tab and handled any optional popup");
    }

    @When("User clicks on {string} button")
    public void userClicksOnButton(String buttonName) {
        if (buttonName.equalsIgnoreCase("Add to cart")) {
            productPage.handleSignInPopup();
            productPage.clickAddToCart();
            LogUtils.info("Clicked on Add to Cart button");
        } else {
            LogUtils.info("Button not implemented: " + buttonName);
        }
    }

    @When("User closes the add to cart popup by clicking the Close button")
    public void userClosesTheAddToCartPopupByClickingTheCloseButton() {
        productPage.closeAddToCartPopup();
        LogUtils.info("Closed the add to cart popup");
    }

    @Then("User should see the cart count updated to {string} item")
    public void userShouldSeeTheCartCountUpdatedTo(String expectedCount) {
        // Switch back to main window to check cart count
        WebUI.switchToMainWindow();
        WebUI.sleep(2);
        
        // Refresh or navigate back to check cart
        WebUI.reloadPage();
        WebUI.sleep(3);
        
        ebayHomePage.verifyCartCount(expectedCount);
        LogUtils.info("Verified cart count updated to: " + expectedCount + " item(s)");
    }


    @Then("User should see product details page")
    public void userShouldSeeProductDetailsPage() {
        productPage.verifyProductPageLoaded();
        productPage.verifyAddToCartButtonPresent();
        LogUtils.info("Verified product details page is loaded");
    }

    @Then("User should see Add to Cart button")
    public void userShouldSeeAddToCartButton() {
        productPage.verifyAddToCartButtonPresent();
        LogUtils.info("Verified Add to Cart button is present");
    }

    // Additional step for getting product information
    @When("User views product title and price")
    public void userViewsProductTitleAndPrice() {
        String title = productPage.getProductTitle();
        String price = productPage.getProductPrice();
        LogUtils.info("Product Title: " + title);
        LogUtils.info("Product Price: " + price);
    }

    // Helper method to verify current page
    @Then("User should be on {string} page")
    public void userShouldBeOnPage(String pageName) {
        switch (pageName.toLowerCase()) {
            case "homepage":
            case "home":
                ebayHomePage.verifyPageLoaded();
                break;
            case "search results":
                searchResultsPage.verifySearchResultsDisplayed("");
                break;
            case "product":
            case "product details":
                productPage.verifyProductPageLoaded();
                break;
            default:
                LogUtils.info("Page verification not implemented for: " + pageName);
        }
    }
}