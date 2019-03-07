Feature: User Profile Validation

  Background:
    Given a user exists with details:
      | Name      | My User                                                                 |
      | Email     | test@example.com                                                        |
      | Providers | google:testuserid-1234567890:My User;twitter:twitter-1234567890:@MyUser |
    Given I load the home page

  @wip
  Scenario Outline: Updating the User Profile with invalid data
    Given I authenticate with "google"
    And I am logged in as "My User"
    And I load the user profile page
    When I set the the user profile details to:
      | Name  | <New Name>  |
      | Email | <New Email> |
    Then the user profile has details:
      | <Invalid Field> is valid?        | False   |
      | <Invalid Field> validation error | <Error> |

    Examples: Individual field errors
      | New Name | New Email        | Invalid Field | Error                    |
      |          | test@example.com | Name          | Screen Name is required  |
      | My User  | test             | Email         | Email Address is invalid |

    Examples: Both fields in error
      | New Name | New Email        | Invalid Field | Error                    |
      |          | test             | Name          | Screen Name is required  |
      |          | test             | Email         | Email Address is invalid |
