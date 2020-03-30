@PartnerConnect
Feature: Partner Connect API Validation Test

  Scenario: Partner Connect Scenario Validation
    Given login into snowflake
    Then access the console
    Then list all databases
    Then list databases in ts partner account
    Then List schemas is ts_partner.freetrial database
    Then List schemas is ts_partner.snowflake sample data database
    Then Show current available roles
    Then Show Grants to USER
    Then Show Managed Accounts
    Then Show Shares
    Then Create ETL integration

  Scenario: ThoughtSpot Trial First Time Login actions
    #Given Login to trial account with short lived token
    #Then Activate trail account user

  Scenario: ThoughtSpot Trial Redirection validation
    Given Login to trial account
    Then validate trial session
    Then validate trial account session info

  Scenario: Partner Connect cleanup actions
    #Then Remove ETL integration
    #Then Logout from Snowflake

  Scenario: Management Console cleanup actions
    #Then get session info
    #Then verify system config
