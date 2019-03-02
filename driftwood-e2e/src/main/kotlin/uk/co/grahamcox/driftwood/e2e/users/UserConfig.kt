package uk.co.grahamcox.driftwood.e2e.users

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.e2e.database.DatabaseField
import uk.co.grahamcox.driftwood.e2e.database.DatabaseSeeder
import java.time.Clock
import java.util.*

/**
 * Spring Configuration for working with users
 */
@Configuration
class UserConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("userSeeder") {
                val clock = ref<Clock>()
                val objectMapper = ref<ObjectMapper>()

                DatabaseSeeder(
                        ref(),
                        """INSERT INTO users
                            (user_id, version, created, updated, name, email, authentication)
                            VALUES
                            (:user_id, :version, :created, :updated, :name, :email, :authentication::jsonb)""",
                        mapOf(
                                "User ID" to DatabaseField(
                                        field = "user_id",
                                        defaultValue = { UUID.randomUUID() }
                                ),
                                "Version" to DatabaseField(
                                        field = "version",
                                        defaultValue = { UUID.randomUUID() }
                                ),
                                "Created" to DatabaseField(
                                        field = "created",
                                        defaultValue = { Date.from(clock.instant()) }
                                ),
                                "Updated" to DatabaseField(
                                        field = "updated",
                                        defaultValue = { Date.from(clock.instant()) }
                                ),
                                "Name" to DatabaseField(
                                        field = "name",
                                        defaultValue = { "Test User" }
                                ),
                                "Email" to DatabaseField(
                                        field = "email",
                                        defaultValue = { "test@example.com" }
                                ),
                                "Providers" to DatabaseField(
                                        field = "authentication",
                                        defaultValue = { "[]" },
                                        converter = {input ->
                                            val providers = input.split(";")
                                                    .map { provider ->
                                                        val (provider, providerId, displayName) = provider.split(":", limit = 3)

                                                        mapOf(
                                                                "provider" to provider,
                                                                "providerId" to providerId,
                                                                "displayName" to displayName
                                                        )
                                                    }

                                            objectMapper.writeValueAsString(providers)
                                        }
                                )
                        )
                )
            }
        }.initialize(context)
    }
}
