Feature: Viewing the User Profile

  Background:
    Given a user exists with details:
      | Name      | My User                              |
      | Email     | test@example.com                     |
      | Providers | google:testuserid-1234567890:My User |
    Given I load the home page

  Scenario: Viewing the User Profile when not logged in
    When I load the user profile page
    Then I get an error that I am not authenticated

  @wip
  Scenario: Viewing the User Profile when logged in
    Given I authenticate with "google"
    When I load the user profile page
    Then the user profile has details:
      | Name  | My User          |
      | Email | test@example.com |
    And the user profile has login providers:
      | Google | My User |
