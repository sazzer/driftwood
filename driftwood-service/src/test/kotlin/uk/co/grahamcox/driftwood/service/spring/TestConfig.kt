package uk.co.grahamcox.driftwood.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.clients.ClientsTestConfig
import uk.co.grahamcox.driftwood.service.users.UsersTestConfig
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

/**
 * Spring config for integration tests
 */
@Configuration
@Import(
        UsersTestConfig::class,
        ClientsTestConfig::class
)
class TestConfig(context: GenericApplicationContext) {
    init {
        beans {
            val now = Instant.now()
            bean("currentTime") { now }

            bean { Clock.fixed(now, ZoneId.of("UTC")) }

            bean {
                DatabaseCleaner(
                        ref(),
                        listOf("flyway_schema_history")
                )
            }
        }.initialize(context)
    }
}
