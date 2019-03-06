Feature: Updating the User Profile

  Background:
    Given a user exists with details:
      | Name      | My User                                                                 |
      | Email     | test@example.com                                                        |
      | Providers | google:testuserid-1234567890:My User;twitter:twitter-1234567890:@MyUser |
    Given I load the home page

  Scenario: Updating the User Profile without saving
    Given I authenticate with "google"
    And I am logged in as "My User"
    And I load the user profile page
    When I set the the user profile details to:
      | Name  | User Name          |
      | Email | test@example.co.uk |
    Then the user profile has details:
      | Name  | User Name          |
      | Email | test@example.co.uk |
    And I am logged in as "My User"

  Scenario: Updating and saving the User Profile
    Given I authenticate with "google"
    And I am logged in as "My User"
    And I load the user profile page
    When I set the the user profile details to:
      | Name  | User Name          |
      | Email | test@example.co.uk |
    And I save the user profile changes
    Then the user profile has details:
      | Name  | User Name          |
      | Email | test@example.co.uk |
    And I am logged in as "User Name"

  Scenario: Updating and canceling the User Profile
    Given I authenticate with "google"
    And I am logged in as "My User"
    And I load the user profile page
    When I set the the user profile details to:
      | Name  | User Name          |
      | Email | test@example.co.uk |
    And I cancel the user profile changes
    Then the user profile has details:
      | Name  | My User          |
      | Email | test@example.com |
    And I am logged in as "My User"
