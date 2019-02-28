package uk.co.grahamcox.driftwood.e2e.browser

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.RemoteWebDriverBuilder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.env.get
import java.net.URI

/**
 * Configuration for the Web Browser to use
 */
@Configuration
class BrowserConfig(context: GenericApplicationContext) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(BrowserConfig::class.java)
    }
    init {
        beans {
            bean<WebDriver> {
                val seleniumUri = env["selenium.url"]
                LOG.debug("Selenium URI: {}", seleniumUri)

                if (seleniumUri.isNullOrBlank()) {
                    LOG.debug("Building Chrome Driver")
                    ChromeDriver()
                } else {
                    LOG.debug("Building Remote Driver: {}", seleniumUri)
                    RemoteWebDriver.builder()
                            .url(seleniumUri)
                            .oneOf(ChromeOptions())
                            .build()
                }
            }
            bean<Browser> {
                val baseUri = env["selenium.baseUrl"]!!
                LOG.debug("Base URL: {}", baseUri)

                Browser(
                        ref(),
                        URI(baseUri)
                )
            }
        }.initialize(context)
    }
}
