package uk.co.grahamcox.driftwood.e2e.pages

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import uk.co.grahamcox.driftwood.e2e.browser.Browser
import uk.co.grahamcox.driftwood.e2e.pages.header.HeaderPageModel

/**
 * The base for all page models
 */
open class BasePage(private val browser: Browser) {
    /** Web element representing the page header */
    @FindBy(css = "#root > div > div.ui.inverted.top.attached.menu")
    private lateinit var headerElement: WebElement

    init {
        PageFactory.initElements(browser.webDriver, this)
    }

    /**
     * Navigate to the page
     */
    protected fun navigateTo(url: String) {
        browser.navigateTo(url)
    }

    /**
     * Get the page model representing the header
     */
    val header: HeaderPageModel
        get() = HeaderPageModel(headerElement)
}
