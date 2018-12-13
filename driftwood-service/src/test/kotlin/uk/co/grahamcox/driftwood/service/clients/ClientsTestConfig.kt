package uk.co.grahamcox.driftwood.service.clients

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.dao.ColumnDetails
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import java.time.Clock
import java.time.Instant
import java.util.*
import javax.sql.DataSource

/**
 * Spring config for testing clients
 */
@Configuration
class ClientsTestConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("clientSeeder") {
                val clock: Clock = ref()
                val dataSource: DataSource = ref()
                fun buildStringArray(strings: List<String>) =
                        dataSource.connection.createArrayOf("text",
                                strings
                                        .filter { it.isNotBlank() }
                                        .toTypedArray())

                DatabaseSeeder(
                        ref(),
                        """INSERT INTO clients(client_id, version, created, updated, name, owner_id, client_secret, redirect_uris, response_types, grant_types)
                            VALUES (:clientId, :version, :created, :updated, :name, :ownerId, :clientSecret, :redirectUris::text[], :responseTypes::text[], :grantTypes::text[])""",
                        mapOf(
                                "clientId" to ColumnDetails(
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
                                "name" to ColumnDetails(
                                        default = { "Test Client" }
                                ),
                                "ownerId" to ColumnDetails(
                                        converter = UUID::fromString
                                ),
                                "clientSecret" to ColumnDetails(
                                        default = UUID::randomUUID,
                                        converter = UUID::fromString
                                ),
                                "redirectUris" to ColumnDetails(
                                        default = { buildStringArray(emptyList()) },
                                        converter = { buildStringArray(it.split(",")) }
                                ),
                                "responseTypes" to ColumnDetails(
                                        default = { buildStringArray(emptyList()) },
                                        converter = { buildStringArray(it.split(",")) }
                                ),
                                "grantTypes" to ColumnDetails(
                                        default = { buildStringArray(emptyList()) },
                                        converter = { buildStringArray(it.split(",")) }
                                )
                        )
                )
            }

        }.initialize(context)
    }
}
