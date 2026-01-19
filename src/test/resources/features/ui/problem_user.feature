Feature: Problem user item add

  Scenario: problem_user adds item from product page
    Given I open the login page
    When I login as "problem_user" with password "secret_sauce"
    And I open product by name "Sauce Labs Backpack"
    And I add the product to the cart from product page
    And I go to the cart
    Then the cart should contain product "Sauce Labs Backpack"
