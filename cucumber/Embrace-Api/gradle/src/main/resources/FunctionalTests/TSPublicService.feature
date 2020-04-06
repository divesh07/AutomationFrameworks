@TSPublicService
Feature: TS Public API Validation

  Scenario: Public services for session management
    Then Login to trial account as user
    Then validate trial session for user
    Then logout to trial account as user