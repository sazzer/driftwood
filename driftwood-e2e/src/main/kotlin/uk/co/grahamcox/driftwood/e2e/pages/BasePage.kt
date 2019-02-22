package uk.co.grahamcox.driftwood.e2e.pages

import org.openqa.selenium.support.PageFactory
import uk.co.grahamcox.driftwood.e2e.browser.Browser

/**
 * The base for all page models
 */
open class BasePage(private val browser: Browser) {

    init {
        PageFactory.initElements(browser.webDriver, this)
    }

    /**
     * Navigate to the page
     */
    protected fun navigateTo(url: String) {
        browser.navigateTo(url)
    }
}
