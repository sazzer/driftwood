package uk.co.grahamcox.driftwood.e2e.pages.header

import org.awaitility.Duration
import org.awaitility.kotlin.await
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import uk.co.grahamcox.driftwood.e2e.selenium.hasClassName

/**
 * Page Model representing the profile menu
 */
class ProfileMenuPageModel(root: WebElement) {
    private val profileMenuElement: WebElement

    /** The web element representing the users name */
    @FindBy(css = "div.text[role=\"alert\"]")
    private lateinit var usernameElement: WebElement

    /** The web element representing the logout menu item */
    @FindBy(css = "[data-test=\"logout\"]")
    private lateinit var logoutElement: WebElement

    init {
        PageFactory.initElements(DefaultElementLocatorFactory(root), this)
        profileMenuElement = root
    }

    /**
     * Get the user name that we are logged in as
     */
    val userName: String
        get() = usernameElement.text

    /**
     * Open the profile menu
     */
    fun open() {
        if (!profileMenuElement.hasClassName("visible")) {
            profileMenuElement.click()
            await.alias("Login Menu Opened")
                    .atMost(Duration.FIVE_HUNDRED_MILLISECONDS)
                    .ignoreException(NoSuchElementException::class.java)
                    .until { profileMenuElement.hasClassName("visible") }
        }
    }

    /**
     * Log out from the system
     */
    fun logout() {
        open()
        logoutElement.click()
    }
}
