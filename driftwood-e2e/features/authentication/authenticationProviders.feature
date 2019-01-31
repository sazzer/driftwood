Feature: Authentication Providers
  Scenario: I can authenticate with the expected providers
    When I load the home page
    Then I am not logged in
    And the authentication options are:
      | google |
