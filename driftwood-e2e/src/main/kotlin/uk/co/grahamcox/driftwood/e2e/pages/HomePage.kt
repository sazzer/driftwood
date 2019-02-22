package uk.co.grahamcox.driftwood.e2e.pages

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import uk.co.grahamcox.driftwood.e2e.browser.Browser

/**
 * Representation of the Home Page
 */
class HomePage(browser: Browser) : BasePage(browser) {
    /** Web element representing the page header */
    @FindBy(css = "#root > div > div.ui.inverted.top.attached.menu")
    private lateinit var headerElement: WebElement

    /**
     * Navigate to the page
     */
    fun navigateTo() {
        navigateTo("/")
    }

    /**
     * Get the page model representing the header
     */
    val header: HeaderPageModel
        get() = HeaderPageModel(headerElement)
}
