Feature: Viewing the User Profile

  Background:
    Given a user exists with details:
      | Name      | My User                              |
      | Email     | test@example.com                     |
      | Providers | google:testuserid-1234567890:My User;twitter:twitter-1234567890:@MyUser |
    Given I load the home page

  Scenario: Viewing the User Profile when not logged in
    When I load the user profile page
    Then I get an error that I am not authenticated

  Scenario: Viewing the User Profile when logged in
    Given I authenticate with "google"
    And I am logged in as "My User"
    When I load the user profile page
    Then the user profile has details:
      | Name  | My User          |
      | Email | test@example.com |
    And the user profile has login providers:
      | Provider | Display Name |
      | Google   | My User      |
      | Twitter  | @MyUser      |
