package uk.co.grahamcox.driftwood.e2e.browser

import org.openqa.selenium.WebDriver
import org.springframework.beans.factory.DisposableBean
import java.net.URI

/**
 * Wrapper around the web browser to use
 */
class Browser(val webDriver: WebDriver, private val baseUrl: URI) : DisposableBean {
    /**
     * Navigate to the page
     */
    fun navigateTo(url: String) {
        webDriver.get(baseUrl.toString())
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
