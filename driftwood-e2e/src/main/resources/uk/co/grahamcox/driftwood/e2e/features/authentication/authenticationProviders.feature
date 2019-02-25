Feature: Authentication Providers

  Scenario: I can authenticate with the expected providers
    When I load the home page
    Then I am not logged in
    And the authentication options are:
      | google |

  Scenario Outline: Authenticate as a new user with <Provider>
    Given I load the home page
    When I authenticate with "<Provider>"
    Then I am logged in as "<User Name>"

  Examples:
    | Provider | User Name |
    | google   | Test User |

  @ignore
  Scenario Outline: Authenticate as an existing user with <Provider>
    Given a user exists with details:
      | Name      | <User Name>                          |
      | Providers | <Provider>:<Provider ID>:<User Name> |
    And I load the home page
    When I authenticate with "<Provider>"
    Then I am logged in as "<User Name>"

    Examples:
      | Provider | Provider ID                | User Name |
      | google   | testuserid-1234567890      | My User   |
