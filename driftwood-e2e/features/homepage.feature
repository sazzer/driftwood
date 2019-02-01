Feature: Load the home page
    Scenario: Loading the home page
        When I load the home page
        Then the page title should be "Driftwood"
        And I am not logged in
