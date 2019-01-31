Feature: Authentication Providers
  Scenario: I can authenticate with the expected providers
    When I load the home page
    Then I am not logged in
    And the authentication options are:
      | google |

  @wip
  Scenario Outline: Authenticate with <Provider>
    When I load the home page
    And I authenticate with "<Provider>"
    Then I am logged in as "<User Name>"

  Examples:
    | Provider | User Name |
    | google   | Test User |
