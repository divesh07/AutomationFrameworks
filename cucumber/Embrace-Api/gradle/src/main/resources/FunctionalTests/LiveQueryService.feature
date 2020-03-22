@LiveQueryService
Feature: Live Query Services API Validation Test

  Scenario: Live Query Services API
    Given fetch Connection types
    Then Get the list of live query connections
    Then Get connection Table Stats
    Then Get connection status
    Then Connect and fetch metadata of Snowflake Connection
    Then Get the configuration properties for Snowflake Connection Type
    Then Export connection as YAML
    Then Remove data from falcon for a given external table
    Then Get the scheduled timely job schedule
    Then Fetch the connection details
    Then Get the list of logical tables that exists in the connection