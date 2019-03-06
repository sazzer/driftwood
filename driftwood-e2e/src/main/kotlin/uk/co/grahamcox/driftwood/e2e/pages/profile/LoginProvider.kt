package uk.co.grahamcox.driftwood.e2e.pages.profile

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory

/**
 * Representation of a single Login Provider in the table of login providers
 */
class LoginProvider(row: WebElement) {
    /** The web element representing the provider name */
    @FindBy(css = "td:nth-child(1)")
    private lateinit var providerNameElement: WebElement

    /** The web element representing the display name */
    @FindBy(css = "td:nth-child(2)")
    private lateinit var displayNameElement: WebElement

    init {
        PageFactory.initElements(DefaultElementLocatorFactory(row), this)
    }

    /** The provider name */
    val providerName: String
        get() = providerNameElement.text

    /** The display name */
    val displayName: String
        get() = displayNameElement.text
}
