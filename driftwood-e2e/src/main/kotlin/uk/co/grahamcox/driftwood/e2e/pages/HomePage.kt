package uk.co.grahamcox.driftwood.e2e.pages

import uk.co.grahamcox.driftwood.e2e.browser.Browser

/**
 * Representation of the Home Page
 */
class HomePage(private val browser: Browser) {
    /**
     * Navigate to the page
     */
    fun navigateTo() {
        browser.navigateTo("/")
    }
}
