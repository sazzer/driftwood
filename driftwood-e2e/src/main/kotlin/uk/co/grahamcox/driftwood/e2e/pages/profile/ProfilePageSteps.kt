package uk.co.grahamcox.driftwood.e2e.pages.profile

import cucumber.api.java8.En
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.driftwood.e2e.browser.Browser

/**
 * Cucumber steps for working with the Profile Page
 */
class ProfilePageSteps : En {
    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    init {
        When("I load the user profile page") {
            ProfilePage(browser).navigateTo()
        }
    }
}
