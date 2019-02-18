package uk.co.grahamcox.driftwood.e2e.browser

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

/**
 * Configuration for the Web Browser to use
 */
@Configuration
class BrowserConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<Browser>()
        }.initialize(context)
    }
}
