@PartnerConnect
Feature: Partner Connect API Validation Test

  Scenario: Partner Connect Scenario Validation
    Given login into snowflake
    Then access the console
    Then use role AccountAdmin
    Then list all databases
    Then list databases in ts partner account
    Then List schemas is ts_partner.freetrial database
    Then List schemas is ts_partner.snowflake sample data database
    Then list view in schema ts_partner.freetrial
    Then list tables in schema ts_partner.freetrial
    Then Show current available roles
    Then Show Grants to USER
    Then Show Managed Accounts
    Then Show Shares
    Then Create ETL integration

  Scenario: ThoughtSpot Trial First Time Login actions
    Given Login to trial account with short lived token
    Then Activate trial account user

  Scenario: ThoughtSpot Trial Login Post Activation
    Then Login to trial account as user
    Then validate trial session for user
    Then logout to trial account as user

  Scenario: Management Console cleanup actions
    Given Login to trial account as admin
    Then validate system config for admin
    Then validate trial session for admin
    Then fetch the user id
    Then fetch the group id
    Then delete trial account user
    Then delete trial account user group
    Then logout to trial account as admin

  Scenario: Partner Connect cleanup actions
    Then Remove ETL integration
    Then Bootstrap data request
    Then MFA Enrollment request
    Then Close Container Button
    Then Logout from Snowflake

