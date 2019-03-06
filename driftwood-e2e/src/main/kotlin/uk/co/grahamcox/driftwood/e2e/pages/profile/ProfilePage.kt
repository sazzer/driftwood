package uk.co.grahamcox.driftwood.e2e.pages.profile

import org.awaitility.Duration
import org.awaitility.kotlin.await
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import uk.co.grahamcox.driftwood.e2e.browser.Browser
import uk.co.grahamcox.driftwood.e2e.pages.BasePage
import uk.co.grahamcox.driftwood.e2e.selenium.hasClassName

/**
 * Representation of the Profile Page
 */
class ProfilePage(browser: Browser) : BasePage(browser) {
    /** The web element representing the form */
    @FindBy(css = "[data-test=\"userProfile\"] form")
    private lateinit var formElement: WebElement

    /** The web element representing the header of the account details section */
    @FindBy(css = "[data-test=\"accountDetailsHeader\"]")
    private lateinit var accountDetailsHeaderElement: WebElement

    /** The web element representing the content of the account details section */
    @FindBy(css = "[data-test=\"accountDetailsContent\"]")
    private lateinit var accountDetailsContentElement: WebElement

    /** The web element representing the header of the login Providers section */
    @FindBy(css = "[data-test=\"loginProvidersHeader\"]")
    private lateinit var loginProvidersHeaderElement: WebElement

    /** The web element representing the content of the login Providers section */
    @FindBy(css = "[data-test=\"loginProvidersContent\"]")
    private lateinit var loginProvidersContentElement: WebElement

    init {
        PageFactory.initElements(DefaultElementLocatorFactory(browser.webDriver), this)
    }

    /**
     * Navigate to the page
     */
    fun navigateTo() {
        navigateTo("/profile")
    }

    /**
     * Get the account details section
     */
    val accountDetails: AccountDetailsPageModel
        get() {
            waitUntilFormLoaded()
            if (!accountDetailsHeaderElement.hasClassName("active")) {
                accountDetailsHeaderElement.click()
            }

            return AccountDetailsPageModel(accountDetailsContentElement)
        }

    /**
     * Get the login providers section
     */
    val loginProviders: LoginProvidersPageModel
        get() {
            waitUntilFormLoaded()
            if (!loginProvidersHeaderElement.hasClassName("active")) {
                loginProvidersHeaderElement.click()
            }

            return LoginProvidersPageModel(loginProvidersContentElement)
        }

    /**
     * Wait until the form has finished loading
     */
    private fun waitUntilFormLoaded() {
        await.alias("Form Loaded")
                .atMost(Duration.FIVE_SECONDS)
                .ignoreException(NoSuchElementException::class.java)
                .until { !formElement.hasClassName("loading") }
    }
}
