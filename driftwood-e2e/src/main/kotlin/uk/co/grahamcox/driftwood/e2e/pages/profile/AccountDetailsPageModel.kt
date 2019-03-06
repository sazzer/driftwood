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
 * Representation of the Account Details section of the Profile Page
 */
class AccountDetailsPageModel(root: WebElement) {
    /** The web element representing the screen name input */
    @FindBy(css = "input[name=\"name\"]")
    private lateinit var screenNameElement: WebElement

    /** The web element representing the email input */
    @FindBy(css = "input[name=\"email\"]")
    private lateinit var emailElement: WebElement

    init {
        PageFactory.initElements(DefaultElementLocatorFactory(root), this)
    }

    /** The current value of the screen name */
    var screenName: String
        get() = screenNameElement.getAttribute("value")
        set(newValue) {
            screenNameElement.clear()
            screenNameElement.sendKeys(newValue)
        }

    /** The current value of the email address */
    var email: String
        get() = emailElement.getAttribute("value")
        set(newValue) {
            emailElement.clear()
            emailElement.sendKeys(newValue)
        }
}
