package com.sdastest.projects.website.ebay.pages;

import com.sdastest.helpers.PropertiesHelpers;
import com.sdastest.utils.LogUtils;
import org.openqa.selenium.By;

import static com.sdastest.keywords.WebUI.*;

public class EbayHomePage {

    private String pageUrl = "https://www.ebay.com";
    private String pageTitle = "Electronics, Cars, Fashion, Collectibles & More | eBay";

    // Load locators from properties file with alternate loators in case of failure
    private By searchBar = By.cssSelector(PropertiesHelpers.getValue("search_bar"));
    private By searchButton = By.cssSelector(PropertiesHelpers.getValue("search_button"));
    private By cartIcon = By.cssSelector(PropertiesHelpers.getValue("cart_icon"));
    private By ebayLogo = By.cssSelector(PropertiesHelpers.getValue("ebay_logo"));
    
    // Alternative locators
    private By searchBarAlt = By.cssSelector("input[placeholder*='Search for anything']");
    private By searchButtonAlt = By.cssSelector(PropertiesHelpers.getValue("search_button_alt"));
    private By searchButtonAlt2 = By.cssSelector(PropertiesHelpers.getValue("search_button_alt2"));
    private By ebayLogoAlt = By.cssSelector(PropertiesHelpers.getValue("ebay_logo_alt"));
    private By ebayLogoAlt2 = By.cssSelector(PropertiesHelpers.getValue("ebay_logo_alt2"));

    public EbayHomePage() {
        PropertiesHelpers.loadAllFiles();
    }

    public void navigateToEbay() {
        openWebsite(pageUrl);
        waitForPageLoaded();
        verifyContains(getCurrentUrl(), "ebay.com", "Failed to navigate to eBay homepage");
        verifyContains(getPageTitle(), "eBay", "eBay page title not match");
    }

    public void searchForItem(String searchTerm) {
        waitForElementVisible(searchBar, 10);
        clearAndFillText(searchBar, searchTerm);
        LogUtils.info("Entered search term: " + searchTerm);
    }

    public EbaySearchResultsPage clickSearchButton() {
        boolean clicked = false;
        
        By[] searchButtonLocators = {searchButton, searchButtonAlt, searchButtonAlt2};
        
        for (By locator : searchButtonLocators) {
            try {
                waitForElementClickable(locator, 5);
                clickElement(locator);
                LogUtils.info("Clicked search button using locator: " + locator);
                clicked = true;
                break;
            } catch (Exception e) {
                LogUtils.info("Failed to click search button with locator: " + locator + " - " + e.getMessage());
                continue;
            }
        }
        
        if (!clicked) {
            // Last resort - press Enter on search bar
            try {
                clickElement(searchBar);
                sendKeys(searchBar, org.openqa.selenium.Keys.ENTER);
                LogUtils.info("Pressed Enter on search bar as fallback");
                clicked = true;
            } catch (Exception e) {
                throw new RuntimeException("Could not click search button with any method");
            }
        }
        
        waitForPageLoaded();
        return new EbaySearchResultsPage();
    }

    public void verifyPageLoaded() {
        // Wait for search bar with fallback
        boolean searchBarFound = false;
        try {
            waitForElementVisible(searchBar, 5);
            searchBarFound = true;
        } catch (Exception e) {
            try {
                waitForElementVisible(searchBarAlt, 5);
                searchBarFound = true;
            } catch (Exception ex) {
                LogUtils.info("Search bar not found with either locator");
            }
        }
        
        boolean logoFound = false;
        try {
            waitForElementVisible(ebayLogo, 2);
            logoFound = true;
        } catch (Exception e) {
            try {
                waitForElementVisible(ebayLogoAlt, 2);
                logoFound = true;
            } catch (Exception ex) {
                try {
                    waitForElementVisible(ebayLogoAlt2, 2);
                    logoFound = true;
                } catch (Exception ex2) {
                    LogUtils.info("eBay logo not found with any locator - continuing anyway (not critical)");
                }
            }
        }
        
        // Verify at least search bar is present
        if (!searchBarFound) {
            throw new AssertionError("Search bar not found on eBay homepage with any locator");
        }
        
        LogUtils.info("eBay homepage verified - Search bar found: " + searchBarFound + ", Logo found: " + logoFound);
    }

    public String getCartCount() {
        waitForElementVisible(cartIcon, 10);

        String cartText = getTextElement(cartIcon);
        // Extract number from text like "Your shopping cart contains 0 items"
        if (cartText.contains("contains")) {
            String[] parts = cartText.split("contains ");
            if (parts.length > 1) {
                String numberPart = parts[1].split(" ")[0];
                return numberPart;
            }
        }
        return cartText;
    }

    public void verifyCartCount(String expectedCount) {
        String actualCount = getCartCount();
        verifyEquals(actualCount, expectedCount, "Cart count does not match expected value");
        LogUtils.info("Cart count verified: " + actualCount + " items");
    }

    public void clickOnCart() {
        waitForElementClickable(cartIcon, 10);
        clickElement(cartIcon);
        waitForPageLoaded();
        LogUtils.info("Clicked on cart icon");
    }
}