package uk.co.grahamcox.driftwood.service.users

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.dao.ColumnDetails
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import java.time.Clock
import java.time.Instant
import java.util.*

/**
 * Spring config for testing users
 */
@Configuration
class UsersTestConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("userSeeder") {
                val clock: Clock = ref()

                DatabaseSeeder(
                        ref(),
                        """INSERT INTO users(user_id, version, created, updated, name, email, authentication)
                            VALUES (:userId, :version, :created, :updated, :name, :email, :logins::jsonb)""",
                        mapOf(
                                "userId" to ColumnDetails(
                                        default = UUID::randomUUID,
                                        converter = UUID::fromString
                                ),
                                "version" to ColumnDetails(
                                        default = UUID::randomUUID,
                                        converter = UUID::fromString
                                ),
                                "created" to ColumnDetails(
                                        default = { Date.from(clock.instant()) },
                                        converter = { Date.from(Instant.parse(it)) }
                                ),
                                "updated" to ColumnDetails(
                                        default = { Date.from(clock.instant()) },
                                        converter = { Date.from(Instant.parse(it)) }
                                ),
                                "name" to ColumnDetails(),
                                "email" to ColumnDetails(),
                                "logins" to ColumnDetails(
                                        default = { "[]" },
                                        converter = { "[]" }
                                )
                        )
                )
            }
        }.initialize(context)
    }
}
