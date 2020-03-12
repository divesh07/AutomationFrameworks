@UserSession
Feature: User Session Services API Validation Test

  Scenario: User Session Services API
    Given validate session login
    Then validate session info
    Then validate session isActive
    Then create a user with weak password
    Then create a user with valid password
    Then create duplicate user
    Then validate session logout

#  Scenario: Connection API



