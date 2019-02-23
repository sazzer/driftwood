package uk.co.grahamcox.driftwood.e2e.pages.header

import org.awaitility.Duration
import org.awaitility.kotlin.await
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebElement
import uk.co.grahamcox.driftwood.e2e.selenium.hasClassName

/**
 * Page Model representing the login menu
 */
class LoginMenuPageModel(private val root: WebElement) {
    /** The list of elements representing the providers */
    private val providerElements
        get() = root.findElements(By.cssSelector("[data-provider]"))

    /** The IDs of the providers */
    val providers: List<String>
        get() {
            open()
            return providerElements.map { provider -> provider.getAttribute("data-provider") }
        }

    /**
     * Open the login menu
     */
    fun open() {
        waitUntilLoaded()
        if (!root.hasClassName("visible")) {
            root.click()
            await.alias("Login Menu Opened")
                    .atMost(Duration.FIVE_HUNDRED_MILLISECONDS)
                    .ignoreException(NoSuchElementException::class.java)
                    .until { root.hasClassName("visible") }
        }
    }

    /**
     * Authenticate with the given provider
     * @param provider The provider to authenticate as
     */
    fun authenticate(provider: String) {
        open()
        providerElements.first { providerElement -> providerElement.getAttribute("data-provider") == provider }
                .click()
    }

    /**
     * Wait until the list of provider elements are loaded
     */
    private fun waitUntilLoaded() {
        await.alias("Login Menu Loaded")
                .atMost(Duration.FIVE_SECONDS)
                .ignoreException(NoSuchElementException::class.java)
                .until {
                    root.isDisplayed && !root.hasClassName("loading") && !root.hasClassName("error")
                }
    }
}
