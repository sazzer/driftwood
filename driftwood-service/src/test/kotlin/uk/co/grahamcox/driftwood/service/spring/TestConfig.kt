package uk.co.grahamcox.driftwood.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

/**
 * Spring config for integration tests
 */
@Configuration
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
