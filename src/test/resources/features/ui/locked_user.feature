Feature: Locked out user

  Scenario: Locked out user cannot login
    Given I open the login page
    When I attempt to login as "locked_out_user" with password "secret_sauce"
    Then login should fail with an error message
