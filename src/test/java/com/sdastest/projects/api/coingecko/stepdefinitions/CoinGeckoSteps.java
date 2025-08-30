package com.sdastest.projects.api.coingecko.stepdefinitions;

import com.sdastest.projects.api.coingecko.client.CoinGeckoAPIClient;
import com.sdastest.utils.LogUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

public class CoinGeckoSteps {
    
    private CoinGeckoAPIClient coinGeckoClient;
    private Response apiResponse;
    private String[] supportedCurrencies = {"USD", "EUR", "GBP"};
    
    public CoinGeckoSteps() {
        coinGeckoClient = new CoinGeckoAPIClient();
    }
    
    @Given("User sends GET request to CoinGecko Bitcoin API endpoint {string}")
    public void userSendsGETRequestToCoinGeckoBitcoinAPIEndpoint(String endpoint) {
        LogUtils.info("Sending GET request to CoinGecko Bitcoin API");
        apiResponse = coinGeckoClient.getBitcoinData();
        LogUtils.info("API request sent successfully to: " + endpoint);
    }
    
    @When("User receives the API response")
    public void userReceivesTheAPIResponse() {
        Assert.assertNotNull(apiResponse, "API response should not be null");
        LogUtils.info("API response received successfully");
        LogUtils.info("Response Status Code: " + apiResponse.getStatusCode());
        LogUtils.info("Response Time: " + apiResponse.getTime() + "ms");
        LogUtils.info("Response Headers: " + apiResponse.getHeaders().toString());
        LogUtils.info("Response Body:");
        LogUtils.info(apiResponse.jsonPath().prettify());
    }
    
    @Then("User should verify the response status code is {int}")
    public void userShouldVerifyTheResponseStatusCodeIs(int expectedStatusCode) {
        coinGeckoClient.validateStatusCode(expectedStatusCode);
        LogUtils.info("Status code validation passed: " + expectedStatusCode);
    }
    
    @Then("User should verify the response contains market data for currency {string}")
    public void userShouldVerifyTheResponseContainsMarketDataForCurrency(String currency) {
        boolean hasMarketData = coinGeckoClient.hasMarketDataForCurrency(currency);
        Assert.assertTrue(hasMarketData, "Market data for " + currency + " should be present in response");
        LogUtils.info("Market data validation passed for currency: " + currency);
    }
    
    @Then("User should verify each currency has market cap data")
    public void userShouldVerifyEachCurrencyHasMarketCapData() {
        for (String currency : supportedCurrencies) {
            boolean hasMarketCap = coinGeckoClient.hasMarketCapForCurrency(currency);
            Assert.assertTrue(hasMarketCap, "Market cap data for " + currency + " should be present");
            LogUtils.info("Market cap data validated for: " + currency);
        }
        LogUtils.info("All currencies have market cap data");
    }
    
    @Then("User should verify each currency has total volume data")
    public void userShouldVerifyEachCurrencyHasTotalVolumeData() {
        for (String currency : supportedCurrencies) {
            boolean hasTotalVolume = coinGeckoClient.hasTotalVolumeForCurrency(currency);
            Assert.assertTrue(hasTotalVolume, "Total volume data for " + currency + " should be present");
            LogUtils.info("Total volume data validated for: " + currency);
        }
        LogUtils.info("All currencies have total volume data");
    }
    
    @Then("User should verify the response contains price change percentage for last 24 hours")
    public void userShouldVerifyTheResponseContainsPriceChangePercentageForLast24Hours() {
        boolean hasPriceChange = coinGeckoClient.hasPriceChangePercentage24h();
        Assert.assertTrue(hasPriceChange, "24h price change percentage should be present in response");
        
        Float priceChange = coinGeckoClient.getPriceChangePercentage24h();
        Assert.assertNotNull(priceChange, "24h price change percentage value should not be null");
        
        LogUtils.info("24h price change percentage validation passed: " + priceChange + "%");
    }
    
    @Then("User should verify homepage URL in the response is not empty")
    public void userShouldVerifyHomepageURLInTheResponseIsNotEmpty() {
        boolean hasHomepage = coinGeckoClient.hasHomepageUrl();
        Assert.assertTrue(hasHomepage, "Homepage URL should be present and not empty");
        
        String homepageUrl = coinGeckoClient.getHomepageUrl();
        Assert.assertNotNull(homepageUrl, "Homepage URL should not be null");
        Assert.assertFalse(homepageUrl.trim().isEmpty(), "Homepage URL should not be empty");
        
        LogUtils.info("Homepage URL validation passed: " + homepageUrl);
    }
    
    @Then("User should verify the response time is less than {int} seconds")
    public void userShouldVerifyTheResponseTimeIsLessThanSeconds(int maxSeconds) {
        long maxResponseTimeMs = maxSeconds * 1000L;
        coinGeckoClient.validateResponseTime(maxResponseTimeMs);
        LogUtils.info("Response time validation passed: " + apiResponse.getTime() + "ms");
    }
    
    @Then("User should verify the response contains valid JSON data")
    public void userShouldVerifyTheResponseContainsValidJSONData() {
        try {
            apiResponse.jsonPath().get();
            LogUtils.info("JSON validation passed - response contains valid JSON");
        } catch (Exception e) {
            Assert.fail("Response does not contain valid JSON: " + e.getMessage());
        }
    }
    
    @Then("User should verify Bitcoin coin ID is {string}")
    public void userShouldVerifyBitcoinCoinIDIs(String expectedId) {
        String actualId = apiResponse.jsonPath().get("id");
        Assert.assertEquals(actualId, expectedId, "Coin ID should match expected value");
        LogUtils.info("Coin ID validation passed: " + actualId);
    }
    
    @Then("User should verify Bitcoin symbol is {string}")
    public void userShouldVerifyBitcoinSymbolIs(String expectedSymbol) {
        String actualSymbol = apiResponse.jsonPath().get("symbol");
        Assert.assertEquals(actualSymbol, expectedSymbol, "Coin symbol should match expected value");
        LogUtils.info("Coin symbol validation passed: " + actualSymbol);
    }
    
    @Then("User should verify Bitcoin name is {string}")
    public void userShouldVerifyBitcoinNameIs(String expectedName) {
        String actualName = apiResponse.jsonPath().get("name");
        Assert.assertEquals(actualName, expectedName, "Coin name should match expected value");
        LogUtils.info("Coin name validation passed: " + actualName);
    }
    
    @Then("User should verify {string} has current price data")
    public void userShouldVerifyCurrencyHasCurrentPriceData(String currency) {
        boolean hasCurrentPrice = coinGeckoClient.hasMarketDataForCurrency(currency);
        Assert.assertTrue(hasCurrentPrice, "Current price data for " + currency + " should be present");
        
        String currencyPath = "market_data.current_price." + currency.toLowerCase();
        Object priceValue = apiResponse.jsonPath().get(currencyPath);
        Assert.assertNotNull(priceValue, "Current price value for " + currency + " should not be null");
        
        LogUtils.info("Current price data validated for " + currency + ": " + priceValue);
    }
    
    @Then("User should verify {string} has market cap data")
    public void userShouldVerifyCurrencyHasMarketCapData(String currency) {
        boolean hasMarketCap = coinGeckoClient.hasMarketCapForCurrency(currency);
        Assert.assertTrue(hasMarketCap, "Market cap data for " + currency + " should be present");
        
        String currencyPath = "market_data.market_cap." + currency.toLowerCase();
        Object marketCapValue = apiResponse.jsonPath().get(currencyPath);
        Assert.assertNotNull(marketCapValue, "Market cap value for " + currency + " should not be null");
        
        LogUtils.info("Market cap data validated for " + currency + ": " + marketCapValue);
    }
    
    @Then("User should verify {string} has total volume data")
    public void userShouldVerifyCurrencyHasTotalVolumeData(String currency) {
        boolean hasTotalVolume = coinGeckoClient.hasTotalVolumeForCurrency(currency);
        Assert.assertTrue(hasTotalVolume, "Total volume data for " + currency + " should be present");
        
        String currencyPath = "market_data.total_volume." + currency.toLowerCase();
        Object volumeValue = apiResponse.jsonPath().get(currencyPath);
        Assert.assertNotNull(volumeValue, "Total volume value for " + currency + " should not be null");
        
        LogUtils.info("Total volume data validated for " + currency + ": " + volumeValue);
    }
}