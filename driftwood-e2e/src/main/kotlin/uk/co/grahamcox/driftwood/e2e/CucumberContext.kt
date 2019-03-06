package uk.co.grahamcox.driftwood.e2e

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.e2e.browser.BrowserConfig
import uk.co.grahamcox.driftwood.e2e.database.DatabaseConfig
import uk.co.grahamcox.driftwood.e2e.pages.profile.ProfilePageConfig
import uk.co.grahamcox.driftwood.e2e.users.UserConfig
import java.time.Clock

/**
 * The main Spring Boot configuration
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(
        BrowserConfig::class,
        DatabaseConfig::class,
        UserConfig::class,
        ProfilePageConfig::class
)
class CucumberContext(context: GenericApplicationContext) {
    init {
        beans {
            bean { Clock.systemUTC() }
        }.initialize(context)
    }
}
