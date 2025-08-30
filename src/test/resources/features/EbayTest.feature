Feature: eBay Book Search and Add to Cart Test

  @Regression @EbayTest @device_Window_11
  Scenario: Search for book, add to cart and verify cart count
    Given User navigates to "https://www.ebay.com"
    When User enters "Book" in the search bar
    And User clicks the search button
    Then User should see search results for "Book"
    When User clicks on the first book result
    And User switches to the new product tab
    And User clicks on "Add to cart" button
    Then User should see the cart count updated to "1" item
