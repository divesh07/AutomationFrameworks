@Test
Feature: Partner Connect API Validation Test

  Scenario: Partner Connect Scenario Validation
    Given login into snowflake
    Then Create ETL integration
    Then Recreate ETL integration

  Scenario: ThoughtSpot Trial First Time Login actions
    Given Login to trial account with short lived token
    Given Re-Login to trial account with short lived token
    Then Login to trial account with invalid short lived token
    Then Validate user activation with invalid token
    Then Activate trial account user
    Then Reactivate same user
    Then Login to trial account with the expired short lived token
    Given Login to trial account with invalid destination
    Given Login to trial account with invalid body

