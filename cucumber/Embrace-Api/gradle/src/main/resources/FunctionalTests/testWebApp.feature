@ApiValidation
Feature: API Validation Test

  Scenario: Session API
    Given validate session login API
    Then validate session info API
    Then validate session isActive API
    Then validate session logout API

#  Scenario: Connection API



