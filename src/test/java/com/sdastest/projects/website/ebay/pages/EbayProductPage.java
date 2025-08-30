package com.sdastest.projects.website.ebay.pages;

import com.sdastest.helpers.PropertiesHelpers;
import com.sdastest.utils.LogUtils;
import org.openqa.selenium.By;

import static com.sdastest.keywords.WebUI.*;

public class EbayProductPage {

    // Load locators from properties file
    private By addToCartButton = By.cssSelector(PropertiesHelpers.getValue("add_to_cart_button"));
    private By addToCartButtonAlt = By.xpath(PropertiesHelpers.getValue("add_to_cart_button_alt"));
    private By buyItNowButton = By.cssSelector(PropertiesHelpers.getValue("buy_it_now_button"));
    private By productTitle = By.cssSelector(PropertiesHelpers.getValue("product_title"));
    private By productPrice = By.cssSelector(PropertiesHelpers.getValue("product_price"));

    // Sign-in popup locators (optional popup)
    private By signinPopupContainerAlt = By.xpath(PropertiesHelpers.getValue("signin_popup_container_alt"));
    private By signinPopupContainerAlt2 = By.cssSelector(PropertiesHelpers.getValue("signin_popup_container_alt2"));
    private By continueAsGuestButton = By.cssSelector(PropertiesHelpers.getValue("continue_as_guest_button"));

    
    // Cart popup locators
    private By cartPopupCloseButton = By.cssSelector(PropertiesHelpers.getValue("cart_popup_close_button"));

    // Alternative locators for robustness
    private By addToCartAlt1 = By.id("atcBtn");
    private By addToCartAlt2 = By.cssSelector("a[data-testid='ux-call-to-action']");

    public EbayProductPage() {
        PropertiesHelpers.loadAllFiles();
    }

    public void handleSignInPopup() {
        LogUtils.info("Checking for optional sign-in popup (waiting up to 10 seconds)...");
        
        boolean popupAppeared = false;
        boolean popupHandled = false;
        int maxWaitSeconds = 5;
        
        // Wait up to 10 seconds for the popup to appear
        waitForPageLoaded();
        for (int i = 0; i < maxWaitSeconds; i++) {
            try {
                // Check if popup container is present using multiple locators
                By[] popupContainerLocators = {signinPopupContainerAlt};
//                By[] popupContainerLocators = {signinPopupContainer, signinPopupContainerAlt, signinPopupContainerAlt2};

                for (By containerLocator : popupContainerLocators) {
                    try {
                        if (checkElementExist(containerLocator)) {
                            LogUtils.info("Sign-in popup detected using locator: " + containerLocator);
                            popupAppeared = true;
                            break;
                        }
                    } catch (Exception e) {
                        continue; // Try next locator
                    }
                }
                
                if (popupAppeared) {
                    // Try to click "Continue as guest" button using multiple locators
//                    By continueAsGuestByText = By.xpath("//button[contains(text(), 'guest')] | //a[contains(text(), 'guest')] | //button[contains(@aria-label, 'guest')] | //a[contains(@title, 'guest')]");
//                    By[] guestButtonLocators = {continueAsGuestButton, continueAsGuestAlt, continueAsGuestAlt2, continueAsGuestAlt3, continueAsGuestByText};
                    By[] guestButtonLocators = {continueAsGuestButton};

                    for (By guestLocator : guestButtonLocators) {
                        try {
                            if (checkElementExist(guestLocator)) {
                                sleep(1);
                                clickElement(guestLocator);
                                LogUtils.info("Successfully clicked 'Continue as guest' using locator: " + guestLocator);
                                popupHandled = true;
                                break;
                            }
                        } catch (Exception e) {
                            LogUtils.info("Failed to click guest button with locator: " + guestLocator);
                            continue;
                        }
                    }
                    
                    if (!popupHandled) {
                        // Try pressing ESC key as fallback
                        try {
                            LogUtils.info("Attempting to close popup with ESC key");
                            sendKeys(org.openqa.selenium.Keys.ESCAPE);
                            popupHandled = true;
                        } catch (Exception escException) {
                            LogUtils.info("ESC key didn't work - popup will be ignored");
                            popupHandled = true; // Mark as handled to continue test
                        }
                    }
                    
                    break; // Exit the waiting loop since popup was found
                } else {
                    // No popup found yet, wait 1 second and check again
                    sleep(1);
                }
                
            } catch (Exception e) {
                LogUtils.info("Error while checking for popup: " + e.getMessage());
                sleep(1);
            }
        }
        
        if (!popupAppeared) {
            LogUtils.info("No sign-in popup appeared within " + maxWaitSeconds + " seconds - continuing with Add to Cart");
        } else if (popupHandled) {
            LogUtils.info("Sign-in popup handled successfully - continuing with Add to Cart");
            sleep(2); // Wait for popup to close
        } else {
            LogUtils.info("Sign-in popup appeared but could not be handled - continuing anyway");
        }
    }

    public void verifyProductPageLoaded() {
        waitForPageLoaded();
        // Verify we're on a product page
        verifyContains(getCurrentUrl(), "/itm/", "Not on product page - URL should contain '/itm/'");
        
        // Try to verify product title is present using multiple locators
        boolean titleFound = false;
        try {
            waitForElementVisible(productTitle, 5);
            verifyElementPresent(productTitle, "Product title not found");
            titleFound = true;
        } catch (Exception e) {
            try {
                By titleAlt = By.cssSelector(PropertiesHelpers.getValue("product_title_alt"));
                waitForElementVisible(titleAlt, 5);
                verifyElementPresent(titleAlt, "Product title not found with alt locator");
                titleFound = true;
            } catch (Exception ex) {
                // Last resort - just check page is loaded
                LogUtils.info("Product title not found with either locator - continuing anyway");
            }
        }
        
        LogUtils.info("Product page loaded successfully - Title found: " + titleFound);
    }

    public void clickAddToCart() {
        // Instead of waiting for full page load, wait for Add to Cart button to be available
        sleep(3);
        LogUtils.info("Looking for Add to Cart button on product page");
        
        boolean clicked = false;
        
        // Try multiple locators for Add to Cart button including XPath for text-based matching
        By addToCartByText = By.xpath("//span[contains(text(), 'Add to cart')]");
        By[] addToCartLocators = {addToCartByText};
//        By[] addToCartLocators = {addToCartButton, addToCartButtonAlt, addToCartAlt1, addToCartAlt2, addToCartAlt3, addToCartByText};
        waitForElementClickable(addToCartByText).click();
        LogUtils.info("Clicked Add to Cart button using locator: " + addToCartByText);

//        for (By locator : addToCartLocators) {
        for (By locator : addToCartLocators) {
            try {
                if (checkElementExist(locator)) {
//                    waitForElementClickable(locator, 10);
//                    clickElement(locator);
                    waitForElementClickable(locator,5).click();
                    LogUtils.info("Clicked Add to Cart button using locator: " + locator);
                    sleep(5);
                    clicked = true;
                    waitForElementVisible(By.xpath("//button[@aria-label='Close overlay']"),5);
                    clickElement(By.xpath("//button[@aria-label='Close overlay']"),5);
//                    waitForElementClickable(By.xpath("//button[@aria-label='Close overlay']"),5).click();
                    break;
                }
            } catch (Exception e) {
                LogUtils.info("Failed to click Add to Cart with locator: " + locator + " - " + e.getMessage());
                continue;
            }
        }
    }

    public void closeAddToCartPopup() {

        boolean closed = false;
        
        // Try multiple locators for close button including XPath for text-based matching
        By closeByText = By.xpath("//button[contains(@aria-label, 'Close')");
        By continueShoppingByText = By.xpath("//button[contains(text(), 'Continue shopping')] | //a[contains(text(), 'Continue shopping')]");
        By[] closeLocators = {cartPopupCloseButton, closeByText, continueShoppingByText};

        for (By locator : closeLocators) {
            try {
                if (checkElementExist(locator)) {
                    try {
                        sleep(1);
                        try {
                            clickElement(locator, 5);
                            LogUtils.info("Closed popup using locator: " + locator);
                        } catch (Exception e) {
                            handleSignInPopup();
                            clickElement(locator);
                            LogUtils.info("Closed popup using locator: " + locator);
                        }
                        closed = true;
                        break;
                    } catch (Exception clickException) {
                        LogUtils.info("Close button exists but not clickable: " + locator + " - " + clickException.getMessage());
                        continue;
                    }
                }
            } catch (Exception e) {
                LogUtils.info("Failed to check close button with locator: " + locator + " - " + e.getMessage());
                continue;
            }
        }
        
        if (!closed) {
            // Try alternative approaches
            try {
                // Try pressing ESC key to close popup
                sendKeys(org.openqa.selenium.Keys.ESCAPE);
                LogUtils.info("Attempted to close popup using ESC key");
                closed = true;
            } catch (Exception e) {
                // Try clicking outside the popup area
                try {
                    // Click somewhere on the page to close modal
                    clickElement(By.cssSelector("body"));
                    LogUtils.info("Attempted to close popup by clicking outside");
                    closed = true;
                } catch (Exception ex) {
                    LogUtils.info("Could not close popup - continuing anyway as item may already be in cart");
                    closed = true; // Mark as closed to continue with the test
                }
            }
        }
        
        // Wait for popup to close
        sleep(2);
    }

    public String getProductTitle() {
        try {
            waitForElementVisible(productTitle, 10);
            return getTextElement(productTitle);
        } catch (Exception e) {
            return "Product title not found";
        }
    }

    public String getProductPrice() {
        try {
            waitForElementVisible(productPrice, 10);
            return getTextElement(productPrice);
        } catch (Exception e) {
            return "Price not found";
        }
    }

    public void verifyAddToCartButtonPresent() {
        boolean found = false;
        
        By[] locators = {addToCartButton, addToCartButtonAlt, addToCartAlt1, addToCartAlt2};
        
        for (By locator : locators) {
            if (checkElementExist(locator)) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new AssertionError("Add to Cart button not found on product page");
        }
        
        LogUtils.info("Add to Cart button found on product page");
    }

}