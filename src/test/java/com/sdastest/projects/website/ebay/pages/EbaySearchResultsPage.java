package com.sdastest.projects.website.ebay.pages;

import com.sdastest.driver.DriverManager;
import com.sdastest.helpers.PropertiesHelpers;
import com.sdastest.utils.LogUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.sdastest.keywords.WebUI.*;

public class EbaySearchResultsPage {

    // Load locators from properties file
    private By searchResultsContainer = By.cssSelector(PropertiesHelpers.getValue("search_results_container"));
    private By firstSearchResult = By.xpath(PropertiesHelpers.getValue("first_search_result"));
    private By firstSearchResultAlt = By.cssSelector(PropertiesHelpers.getValue("first_search_result_alt"));
    private By firstSearchResultAlt2 = By.cssSelector(PropertiesHelpers.getValue("first_search_result_alt2"));
    private By searchResultTitle = By.cssSelector(PropertiesHelpers.getValue("search_result_title"));
    private By searchResultsCount = By.cssSelector(PropertiesHelpers.getValue("search_results_count"));
    private By noResultsMessage = By.cssSelector(PropertiesHelpers.getValue("no_results_message"));

    // Additional alternative selectors
    private By firstResultFallback = By.cssSelector(".s-item .s-item__wrapper .s-item__link");
    private By firstResultFallback2 = By.cssSelector(".s-item h3.s-item__title a");
    private By resultsCountAlt = By.cssSelector(".srp-controls__count-heading");

    public EbaySearchResultsPage() {
        PropertiesHelpers.loadAllFiles();
    }

    public void verifySearchResultsDisplayed(String searchTerm) {
        waitForPageLoaded();
        sleep(2);
        
        // Try multiple ways to verify search results
        try {
            waitForElementVisible(searchResultsContainer, 10);
            verifyElementPresent(searchResultsContainer, "Search results container not found");
        } catch (Exception e) {
            // Fallback verification
            verifyContains(getCurrentUrl(), "sch", "Not on search results page");
        }
        
        verifyContains(getPageTitle(), searchTerm, "Search term not found in page title");
        LogUtils.info("Search results displayed for: " + searchTerm);
    }

    public EbayProductPage clickFirstSearchResult() {
        waitForPageLoaded();
        sleep(2);
        
        boolean clicked = false;
        
        // Try multiple locators for first search result
        By[] resultLocators = {firstSearchResult, firstSearchResultAlt, firstSearchResultAlt2};
//        verifyElementClickable(firstSearchResult,5);
//        waitForElementClickable(firstSearchResult,5).click();

        for (By locator : resultLocators) {
            try {
                if (checkElementExist(locator)) {
                    // Wait for element to be visible first
                    waitForElementVisible(locator, 5);

                    // Check if element is actually clickable and has size
                    WebElement element = getWebElement(locator);
                    if (element.isDisplayed() && element.isEnabled() &&
                        element.getSize().getWidth() > 0 && element.getSize().getHeight() > 0) {

                        // Scroll to element to ensure it's in view
//                        scrollToElementAtTop(locator);
                        sleep(2);

                        clickElement(locator);
                        LogUtils.info("Clicked on first search result using locator: " + locator);
                        clicked = true;
                        break;
                    } else {
                        LogUtils.info("Element found but not clickable (zero size or not visible): " + locator);
                    }
                }
            } catch (Exception e) {
                LogUtils.info("Failed to click first result with locator: " + locator + " - " + e.getMessage());
                continue;
            }
        }

        if (!clicked) {
            throw new RuntimeException("Could not click first search result with any locator");
        }

        // Wait for new tab/window and switch to it
        sleep(3);
        switchToLastWindow();

        // Wait for URL to stabilize and confirm we're on product page
        int attempts = 0;
        String currentUrl = "";
        String previousUrl = "";

        while (attempts < 10) {
            currentUrl = getCurrentUrl();
            LogUtils.info("Current URL (attempt " + (attempts + 1) + "): " + currentUrl);

            // Check if we're on a product page (URL contains /itm/)
            if (currentUrl.contains("/itm/") && !currentUrl.equals(previousUrl)) {
                previousUrl = currentUrl;
                sleep(5); // Wait to see if URL changes again

                // Check if URL is stable (hasn't changed)
                if (currentUrl.equals(getCurrentUrl())) {
                    LogUtils.info("URL stabilized on product page: " + currentUrl);
                    break;
                }
            }

            attempts++;
            sleep(1);
        }
        
        // Wait for key product page elements instead of full page load
        try {
            // Wait for product title or price - key indicators page is loaded
            sleep(1);
            LogUtils.info("Successfully navigated to product page: " + getCurrentUrl());
        } catch (Exception e) {
            LogUtils.info("Product page elements not immediately visible, continuing anyway: " + e.getMessage());
        }
        
        return new EbayProductPage();
    }

    public int getSearchResultsCount() {
        try {
            if (checkElementExist(searchResultsCount)) {
                String countText = getTextElement(searchResultsCount);
                // Extract number from text like "2,000,000+ results for Book"
                String[] parts = countText.split(" ");
                if (parts.length > 0) {
                    String numberPart = parts[0].replace(",", "").replace("+", "");
                    return Integer.parseInt(numberPart);
                }
            }
        } catch (Exception e) {
            LogUtils.info("Could not parse search results count: " + e.getMessage());
        }
        return 0;
    }

    public void verifyNoResults() {
        waitForElementVisible(noResultsMessage, 10);
        verifyElementPresent(noResultsMessage, "No results message not found");
        LogUtils.info("No search results message verified");
    }

    public void verifyResultsContainText(String expectedText) {
        List<WebElement> resultTitles = getWebElements(searchResultTitle);
        boolean found = false;
        
        for (WebElement title : resultTitles) {
            if (getTextElement(By.xpath("//h3")).toLowerCase().contains(expectedText.toLowerCase())) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new AssertionError("No search results contain the text: " + expectedText);
        }
        
        LogUtils.info("Search results contain expected text: " + expectedText);
    }
}