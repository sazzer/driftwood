package uk.co.grahamcox.driftwood.e2e.browser

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.springframework.beans.factory.DisposableBean
import org.springframework.web.util.UriComponentsBuilder
import java.io.File
import java.net.URI

/**
 * Wrapper around the web browser to use
 */
class Browser(val webDriver: WebDriver, private val baseUrl: URI) : DisposableBean {
    /**
     * Reset the browser
     */
    fun reset() {
        navigateTo("/")

        (webDriver as JavascriptExecutor).run {
            executeScript("window.sessionStorage.clear();")
            executeScript("window.localStorage.clear();")
        }
    }

    /**
     * Save a screenshot of the current browser to the given path
     * @param path The path to save the screenshot to
     */
    fun saveScreenshot(path: String) {
        val screenshot = (webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES)
        File(path).writeBytes(screenshot)
    }

    /**
     * Navigate to the page
     */
    fun navigateTo(url: String) {
        val targetUrl = UriComponentsBuilder.fromUri(baseUrl)
                .path(url)
                .build()
                .toString()
        webDriver.get(targetUrl)
    }

    /** Get the title of the current page */
    val title: String
        get() = webDriver.title

    /**
     * Invoked by the containing `BeanFactory` on destruction of a bean.
     * @throws Exception in case of shutdown errors. Exceptions will get logged
     * but not rethrown to allow other beans to release their resources as well.
     */
    override fun destroy() {
        webDriver.quit()
    }
}
