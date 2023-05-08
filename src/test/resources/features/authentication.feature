Feature: Authentication Alta Shop API

  @Authentication @Positive @Register
  Scenario: User register
    Given User call api "/api/auth/register" with method "POST" with payload
      | email       | password       | fullname       |
      | randomEmail | randomPassword | randomFullName |
    And User verify status code is 200

  @Authentication @Positive @Login @ValidUsernameAndPassword
  Scenario: User login with correct username and password
    Given User call api "/api/auth/login" with method "POST" with payload
      | email     | password     |
      | userEmail | userPassword |
    And User verify status code is 200

  @Authentication @Positive @Get
  Scenario: User get user infromation
    Given User call api "/api/auth/login" with method "POST" with payload
      | email     | password     |
      | userEmail | userPassword |
    And User get auth token
    And User get user information
    And User verify status code is 200

  @Authentication @Negative @Login @InvalidUsernameAndPassword
  Scenario: User login with invalid username and password
    Given User call api "/api/auth/login" with method "POST" with payload
      | email       | password       |
      | randomEmail | randomPassword |
    And User verify status code is 400
