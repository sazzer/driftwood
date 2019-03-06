package uk.co.grahamcox.driftwood.e2e.pages.profile

import cucumber.api.java8.En
import io.cucumber.datatable.DataTable
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.driftwood.e2e.browser.Browser
import uk.co.grahamcox.driftwood.e2e.matcher.BeanMatcher
import uk.co.grahamcox.driftwood.e2e.matcher.ListMatcher

/**
 * Cucumber steps for working with the Profile Page
 */
class ProfilePageSteps : En {
    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    /** The bean matcher for the account details */
    @Autowired
    private lateinit var accountDetailsBeanMatcher: BeanMatcher<AccountDetailsPageModel>

    /** The list matcher for the login providers */
    @Autowired
    private lateinit var loginProvidersMatcher: ListMatcher<LoginProvider>

    init {
        When("I load the user profile page") {
            ProfilePage(browser).navigateTo()
        }

        Then("the user profile has details:") { dataTable: DataTable ->
            val accountDetails = ProfilePage(browser).accountDetails
            accountDetailsBeanMatcher.match(dataTable.asMap(String::class.java, String::class.java), accountDetails)
        }

        Then("the user profile has login providers:") { dataTable: DataTable ->
            val loginProviders = ProfilePage(browser).loginProviders.loginProviders
            loginProvidersMatcher.matchExact(dataTable.asMaps(), loginProviders)
        }
    }
}
