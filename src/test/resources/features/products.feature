Feature: Products Alta Shop API

  @Products @Positive
  Scenario: User get all products
    Given User call api "/api/products" with method "GET"
    And User verify status code is 200

  @Products @Positive @Make
  Scenario: User create a new product
    Given User call api "/api/auth/login" with method "POST" with payload
      | email     | password     |
      | userEmail | userPassword |
    And User make a new product
    And User verify status code is 200
    Then User verify response "createProduct.json"

  @Products @Positive
  Scenario: User get products by id
    Given User call api "/api/products/13568" with method "GET"
    And User verify status code is 200

  @Products @Positive
  Scenario: User delete products
    Given User User call api "/api/products/11309" with method "DELETE"
    And User verify status code is 200


  @Products @Positive @Ratings
  Scenario: User assign ratings
    Given User call api "/api/auth/login" with method "POST" with payload
      | email     | password     |
      | userEmail | userPassword |
    And User get auth token
    And User assign ratings
    And User verify status code is 200

  @Products @Positive
  Scenario: User get product ratings
    Given User call api "/api/products/1/ratings" with method "GET"
    And User verify status code is 200


  @Products @Positive @Comments
  Scenario: User create a comment for product
    Given User call api "/api/auth/login" with method "POST" with payload
      | email     | password     |
      | userEmail | userPassword |
    And User get auth token
    And User create a comment
    And User verify status code is 200
    Then User verify response "createComment.json"

  @Products @Positive
  Scenario: User get comment
    Given User call api "/api/products/2/comments" with method "GET"
    And User verify status code is 200