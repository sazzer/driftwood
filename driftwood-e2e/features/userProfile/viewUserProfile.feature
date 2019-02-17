Feature: Viewing the User Profile

  Background:
    Given a user exists with details:
      | Name      | My User                              |
      | Email     | test@example.com                     |
      | Providers | google:testuserid-1234567890:My User |
    
  Scenario: Viewing the User Profile when not logged in
    Given I load the user profile page
    Then I get an error that I am not authenticated
