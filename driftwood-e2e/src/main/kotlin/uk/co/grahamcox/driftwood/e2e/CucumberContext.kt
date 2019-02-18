package uk.co.grahamcox.driftwood.e2e

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import
import uk.co.grahamcox.driftwood.e2e.browser.BrowserConfig

/**
 * The main Spring Boot configuration
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(
        BrowserConfig::class
)
class CucumberContext
