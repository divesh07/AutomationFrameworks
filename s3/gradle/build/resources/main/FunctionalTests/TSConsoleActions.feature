@PartnerConnect
Feature: Partner Connect API Validation Test

  Scenario: ThoughtSpot Trial First Time Login actions
    Given Login to trial account with short lived token
    Then Activate trail account user
