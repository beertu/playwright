Feature: Sorting

  Scenario: Sort products by name ascending
    Given I open the login page
    When I login as "standard_user" with password "secret_sauce"
    And I sort products by "Name (A to Z)"
    Then products should be sorted by name ascending
