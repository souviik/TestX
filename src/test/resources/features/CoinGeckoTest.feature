Feature: CoinGecko Bitcoin API Testing

  @Regression @APITest @CoinGecko
  Scenario: Verify Bitcoin API response contains market data for multiple currencies
    Given User sends GET request to CoinGecko Bitcoin API endpoint "https://api.coingecko.com/api/v3/coins/bitcoin"
    When User receives the API response
    Then User should verify the response status code is 200
    And User should verify the response contains market data for currency "USD"
    And User should verify the response contains market data for currency "EUR"
    And User should verify the response contains market data for currency "GBP"
    And User should verify each currency has market cap data
    And User should verify each currency has total volume data
    And User should verify the response contains price change percentage for last 24 hours
    And User should verify homepage URL in the response is not empty

  @Regression @APITest @device_Window_11
  Scenario Outline: Verify market data exists for different currencies
    Given User sends GET request to CoinGecko Bitcoin API endpoint "https://api.coingecko.com/api/v3/coins/bitcoin"
    When User receives the API response
    Then User should verify the response status code is 200
    And User should verify the response contains market data for currency "<currency>"
    And User should verify "<currency>" has current price data
    And User should verify "<currency>" has market cap data
    And User should verify "<currency>" has total volume data
    Examples:
      | currency |
      | USD      |
      | EUR      |
      | GBP      |