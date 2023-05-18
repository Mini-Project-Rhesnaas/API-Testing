Feature: Orders Alta Shop API


  @Orders @NewOrder
  Scenario: User create a new order
    Given User call api "/api/auth/login" with method "POST" with payload
      | email     | password     |
      | userEmail | userPassword |
    And User get auth token
    And User make new order
    And User verify status code is 200
    Then User verify response "newOrder.json"

  @Orders
  Scenario: User get all orders
    Given User call api "/api/auth/login" with method "POST" with payload
      | email     | password     |
      | userEmail | userPassword |
    And User get auth token
    And User call api "/api/orders" with method "GET"
    And User verify status code is 200
    Then User verify response "allOrders.json"

  @Orders @ID
  Scenario: User get orders by id
    Given User call api "/api/auth/login" with method "POST" with payload
      | email     | password     |
      | userEmail | userPassword |
    And User get auth token
    And User call api "/api/orders/11471" with method "GET"
    And User verify status code is 200
    Then User verify response "byIDOrder.json"