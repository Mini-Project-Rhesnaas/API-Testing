Feature: Product Categories Alta Shop API

  @ProductCategories @Positive
  Scenario: User create a category
    Given User call api "/api/categories" with method "POST" with payload
      | name             | description                 |
      | randomCategories | randomCategoriesDescription |
    And User verify status code is 200
    Then User verify response "createCategory.json"

  @ProductCategories @Positive
  Scenario: User get category by id
    Given User call api "/api/categories/12569" with method "GET"
    And User verify status code is 200
    Then User verify response "createCategory.json"

  @ProductCategories @Positive
  Scenario: User get category
    Given User call api "/api/categories" with method "GET"
    And User verify status code is 200
    Then User verify response "allCategory.json"

  @ProductCategories @Positive
  Scenario: User delete a category
    Given User call api "/api/categories/1" with method "DELETE"
    And User verify status code is 200