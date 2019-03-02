package uk.co.grahamcox.driftwood.e2e.pages.profile

import uk.co.grahamcox.driftwood.e2e.browser.Browser
import uk.co.grahamcox.driftwood.e2e.pages.BasePage

/**
 * Representation of the Profile Page
 */
class ProfilePage(browser: Browser) : BasePage(browser) {

    /**
     * Navigate to the page
     */
    fun navigateTo() {
        navigateTo("/profile")
    }
}
