package uk.co.grahamcox.driftwood.e2e.pages.header

import org.awaitility.Duration
import org.awaitility.kotlin.await
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory

/**
 * Page model representing the header of the page
 */
class HeaderPageModel(root: WebElement) {
    /** The web element representing the login menu */
    @FindBy(css = "[data-test=\"loginMenu\"]")
    private lateinit var loginMenuElement: WebElement

    /** The web element representing the profile menu */
    @FindBy(css = "[data-test=\"profileMenu\"]")
    private lateinit var profileMenuElement: WebElement

    init {
        PageFactory.initElements(DefaultElementLocatorFactory(root), this)
    }

    /**
     * Get whether we are logged in - i.e. the Login Menu is not visible
     */
    val loggedIn: Boolean
        get() = !loginMenuElement.isDisplayed

    /**
     * Get the login menu
     */
    val loginMenu: LoginMenuPageModel
        get() = LoginMenuPageModel(loginMenuElement)

    /**
     * Get the profile menu
     */
    val profileMenu: ProfileMenuPageModel
        get() {
            await.alias("Profile Menu Visible")
                    .atMost(Duration.FIVE_SECONDS)
                    .ignoreException(NoSuchElementException::class.java)
                    .until { profileMenuElement.isDisplayed }

            return ProfileMenuPageModel(profileMenuElement)
        }
}
