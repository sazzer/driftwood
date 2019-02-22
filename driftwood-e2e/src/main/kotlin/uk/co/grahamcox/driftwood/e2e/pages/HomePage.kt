package uk.co.grahamcox.driftwood.e2e.pages

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import uk.co.grahamcox.driftwood.e2e.browser.Browser
import uk.co.grahamcox.driftwood.e2e.pages.header.HeaderPageModel

/**
 * Representation of the Home Page
 */
class HomePage(browser: Browser) : BasePage(browser) {

    /**
     * Navigate to the page
     */
    fun navigateTo() {
        navigateTo("/")
    }
}
