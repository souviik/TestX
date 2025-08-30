package com.sdastest.projects.api.coingecko.client;

import com.sdastest.projects.api.base.BaseAPIClient;
import com.sdastest.utils.LogUtils;
import io.restassured.response.Response;

public class CoinGeckoAPIClient extends BaseAPIClient {
    
    private static final String BASE_URL = "https://api.coingecko.com";
    private static final String API_VERSION = "/api/v3";
    private static final String COINS_ENDPOINT = "/coins";
    
    public Response getBitcoinData() {
        String endpoint = API_VERSION + COINS_ENDPOINT + "/bitcoin";
        LogUtils.info("Fetching Bitcoin data from CoinGecko API");
        return sendGETRequest(BASE_URL, endpoint);
    }
    
    public Response getCoinData(String coinId) {
        String endpoint = API_VERSION + COINS_ENDPOINT + "/" + coinId;
        LogUtils.info("Fetching " + coinId + " data from CoinGecko API");
        return sendGETRequest(BASE_URL, endpoint);
    }
    
    public void validateBitcoinResponse() {
        validateStatusCode(200);
        validateResponseTime(10000); // 10 seconds max
        LogUtils.info("Bitcoin API response validation completed successfully");
    }
    
    public boolean hasMarketDataForCurrency(String currency) {
        Response response = getLastResponse();
        String currencyPath = "market_data.current_price." + currency.toLowerCase();
        
        try {
            Object value = response.jsonPath().get(currencyPath);
            boolean exists = value != null;
            LogUtils.info("Market data for " + currency + " exists: " + exists);
            return exists;
        } catch (Exception e) {
            LogUtils.error("Error checking market data for " + currency + ": " + e.getMessage());
            return false;
        }
    }
    
    public boolean hasMarketCapForCurrency(String currency) {
        Response response = getLastResponse();
        String currencyPath = "market_data.market_cap." + currency.toLowerCase();
        
        try {
            Object value = response.jsonPath().get(currencyPath);
            boolean exists = value != null;
            LogUtils.info("Market cap for " + currency + " exists: " + exists);
            return exists;
        } catch (Exception e) {
            LogUtils.error("Error checking market cap for " + currency + ": " + e.getMessage());
            return false;
        }
    }
    
    public boolean hasTotalVolumeForCurrency(String currency) {
        Response response = getLastResponse();
        String currencyPath = "market_data.total_volume." + currency.toLowerCase();
        
        try {
            Object value = response.jsonPath().get(currencyPath);
            boolean exists = value != null;
            LogUtils.info("Total volume for " + currency + " exists: " + exists);
            return exists;
        } catch (Exception e) {
            LogUtils.error("Error checking total volume for " + currency + ": " + e.getMessage());
            return false;
        }
    }
    
    public boolean hasPriceChangePercentage24h() {
        Response response = getLastResponse();
        
        try {
            Object value = response.jsonPath().get("market_data.price_change_percentage_24h");
            boolean exists = value != null;
            LogUtils.info("24h price change percentage exists: " + exists);
            return exists;
        } catch (Exception e) {
            LogUtils.error("Error checking 24h price change percentage: " + e.getMessage());
            return false;
        }
    }
    
    public boolean hasHomepageUrl() {
        Response response = getLastResponse();
        
        try {
            Object homepageArray = response.jsonPath().get("links.homepage");
            boolean exists = homepageArray != null;
            
            if (exists) {
                String firstHomepage = response.jsonPath().get("links.homepage[0]");
                exists = firstHomepage != null && !firstHomepage.trim().isEmpty();
            }
            
            LogUtils.info("Homepage URL exists and is not empty: " + exists);
            return exists;
        } catch (Exception e) {
            LogUtils.error("Error checking homepage URL: " + e.getMessage());
            return false;
        }
    }
    
    public String getHomepageUrl() {
        Response response = getLastResponse();
        try {
            return response.jsonPath().get("links.homepage[0]");
        } catch (Exception e) {
            LogUtils.error("Error getting homepage URL: " + e.getMessage());
            return null;
        }
    }
    
    public Float getPriceChangePercentage24h() {
        Response response = getLastResponse();
        try {
            return response.jsonPath().get("market_data.price_change_percentage_24h");
        } catch (Exception e) {
            LogUtils.error("Error getting 24h price change percentage: " + e.getMessage());
            return null;
        }
    }
}