package uk.co.grahamcox.driftwood.e2e.pages

import cucumber.api.java8.En
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.driftwood.e2e.browser.Browser

/**
 * Cucumber steps for the Home Page
 */
class HomePageSteps : En {
    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    init {
        When("I load the home page") {
            HomePage(browser).navigateTo()
        }
    }
}
