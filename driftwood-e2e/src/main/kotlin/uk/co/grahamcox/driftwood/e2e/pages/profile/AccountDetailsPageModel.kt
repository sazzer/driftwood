package uk.co.grahamcox.driftwood.e2e.pages.profile

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory
import uk.co.grahamcox.driftwood.e2e.selenium.hasClassName
import uk.co.grahamcox.driftwood.e2e.selenium.setValue

/**
 * Representation of the Account Details section of the Profile Page
 */
class AccountDetailsPageModel(root: WebElement) {
    /** The web element representing the screen name input */
    @FindBy(css = "input[name=\"name\"]")
    private lateinit var screenNameElement: WebElement
    
    /** The web element representing the validation messages for the screen name input */
    @FindBy(css = "div[data-test=\"nameMessages\"]")
    private lateinit var screenNameValidationElement: WebElement

    /** The web element representing the email input */
    @FindBy(css = "input[name=\"email\"]")
    private lateinit var emailElement: WebElement
    
    /** The web element representing the validation messages for the email input */
    @FindBy(css = "div[data-test=\"emailMessages\"]")
    private lateinit var emailValidationElement: WebElement

    init {
        PageFactory.initElements(DefaultElementLocatorFactory(root), this)
    }

    /** The current value of the screen name */
    var screenName: String
        get() = screenNameElement.getAttribute("value")
        set(newValue) {
            screenNameElement.setValue(newValue)
        }

    /** Get whether the screen name is valid or not */
    val screenNameValid: Boolean
        get() = !screenNameValidationElement.isDisplayed && 
                !screenNameValidationElement.hasClassName("error")
    
    /** Get the text for the screen name messages */
    val screenNameMessages: String
        get() = screenNameValidationElement.text
    
    /** The current value of the email address */
    var email: String
        get() = emailElement.getAttribute("value")
        set(newValue) {
            emailElement.setValue(newValue)
        }

    /** Get whether the email is valid or not */
    val emailValid: Boolean
        get() = !emailValidationElement.isDisplayed &&
                !emailValidationElement.hasClassName("error")

    /** Get the text for the email messages */
    val emailMessages: String
        get() = emailValidationElement.text
}
