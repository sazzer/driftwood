Feature: Logging out
  Scenario: Logging out
    Given I load the home page
    And I authenticate with "google"
    When I log out
    And I am not logged in
