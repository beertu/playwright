Feature: Delay parameter tests

  @parametrized
  Scenario Outline: Delay parameter response time
    Given I request products with delay <delay>
    Then the response status should be 200
    And the response time should be less than 8000 ms

    Examples:
      | delay |
      | 0     |
      | 5000  |
