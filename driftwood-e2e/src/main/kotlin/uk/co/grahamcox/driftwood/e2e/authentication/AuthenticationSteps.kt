package uk.co.grahamcox.driftwood.e2e.authentication

import cucumber.api.java8.En
import io.cucumber.datatable.DataTable
import org.junit.Assert
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.driftwood.e2e.browser.Browser
import uk.co.grahamcox.driftwood.e2e.pages.BasePage
import uk.co.grahamcox.driftwood.e2e.pages.HomePage

/**
 * Cucumber steps for handling authentication
 */
class AuthenticationSteps : En {
    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    init {
        Then("I am not logged in") {
            Assert.assertFalse(BasePage(browser).header.loggedIn)
        }

        Then("the authentication options are:") { dataTable: DataTable ->
            Assert.assertEquals(dataTable.asList(), BasePage(browser).header.loginMenu.providers)
        }
    }
}
