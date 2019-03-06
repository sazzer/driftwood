package uk.co.grahamcox.driftwood.e2e.pages.profile

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import uk.co.grahamcox.driftwood.e2e.browser.Browser
import uk.co.grahamcox.driftwood.e2e.pages.BasePage

/**
 * Representation of the Login Providers section of the Profile Page
 */
class LoginProvidersPageModel(private val root: WebElement) {

    /**
     * Get the list of login providers
     */
    val loginProviders: List<LoginProvider>
        get() {
            val loginProviderElements = root.findElements(By.cssSelector("table tr"))
            return loginProviderElements.map(::LoginProvider)
        }
}
