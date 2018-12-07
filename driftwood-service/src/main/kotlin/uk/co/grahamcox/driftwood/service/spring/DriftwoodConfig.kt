package uk.co.grahamcox.driftwood.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.VersionController
import java.time.Clock

/**
 * The main Spring configuration
 */
@Configuration
@Import(
        DatabaseConfig::class
)
class DriftwoodConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean { Clock.systemUTC() }
            bean<VersionController>()
        }.initialize(context)
    }
}
