package uk.co.grahamcox.driftwood.e2e.browser

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.RemoteWebDriverBuilder
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
    init {
        beans {
            bean<WebDriver> {
                val seleniumUri = env["selenium.uri"]

                if (seleniumUri.isNullOrBlank()) {
                    ChromeDriver()
                } else {
                    RemoteWebDriver.builder()
                            .url(seleniumUri)
                            .build()
                }
            }
            bean<Browser> {
                val baseUri = env["selenium.baseUrl"]!!

                Browser(
                        ref(),
                        URI(baseUri)
                )
            }
        }.initialize(context)
    }
}
