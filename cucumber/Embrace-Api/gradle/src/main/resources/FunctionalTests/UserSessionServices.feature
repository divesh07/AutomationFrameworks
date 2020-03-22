@UserSession
Feature: User Session Services API Validation Test

  Scenario: User Session Services API
    Given validate session login
    Then validate session info
    Then validate session isActive
    Then get session privileges
    Then get user guid of the current session
    Then create a user with weak password
    Then create duplicate user
    Then create a group
    Then list member users for a group
    Then fetch mindtouch authentication token
    Then create a user with valid password
    Then delete an existing user
    Then validate session logout





