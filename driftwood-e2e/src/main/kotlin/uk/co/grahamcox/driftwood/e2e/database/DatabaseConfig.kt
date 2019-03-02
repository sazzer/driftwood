package uk.co.grahamcox.driftwood.e2e.database

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

/**
 * The configuration for the database connection
 * @param context The Spring context
 */
@Configuration
class DatabaseConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<DatabaseCleaner>()
        }.initialize(context)
    }
}
