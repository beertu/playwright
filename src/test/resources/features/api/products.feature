Feature: DummyJSON products CRUD

  Scenario: Get all products and print odd id titles
    Given I request all products
    Then the response status should be 200
    And print titles of products with odd ids

  Scenario: Create a new product
    Given I create a product with title "My Product" description "desc" price 99 brand "MyBrand"
    Then the response status should be 201
    And the response should contain the created product title "My Product"

  Scenario: Get third product and update it
    Given I get product with id 3
    Then the response status should be 200
    When I update product id 3 with title "Updated Title"
    Then the update response status should be 200
    And the updated product title should be "Updated Title"
