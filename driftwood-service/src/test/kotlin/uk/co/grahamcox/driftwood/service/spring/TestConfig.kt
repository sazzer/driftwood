package uk.co.grahamcox.driftwood.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributesTestConfig
import uk.co.grahamcox.driftwood.service.clients.ClientsTestConfig
import uk.co.grahamcox.driftwood.service.users.UsersTestConfig

/**
 * Spring config for integration tests
 */
@Configuration
@Import(
        UsersTestConfig::class,
        ClientsTestConfig::class,
        AttributesTestConfig::class
)
class TestConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean {
                DatabaseCleaner(
                        ref(),
                        listOf("flyway_schema_history")
                )
            }
        }.initialize(context)
    }
}
