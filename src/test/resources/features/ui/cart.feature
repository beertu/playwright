Feature: Cart flow

  Scenario: Add all items, remove third, checkout and confirm order
    Given I open the login page
    When I login as "standard_user" with password "secret_sauce"
    And I add all products to the cart
    And I go to the cart
    And I remove the third item from the cart
    And I proceed to checkout with firstName "John" lastName "Doe" postalCode "12345"
    Then I should see the order confirmation
