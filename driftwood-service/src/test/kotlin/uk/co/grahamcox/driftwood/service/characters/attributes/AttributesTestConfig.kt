package uk.co.grahamcox.driftwood.service.characters.attributes

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.dao.ColumnDetails
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import java.time.Clock
import java.time.Instant
import java.util.*

/**
 * Spring config for testing attributes
 */
@Configuration
class AttributesTestConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean("attributeSeeder") {
                val clock: Clock = ref()

                DatabaseSeeder(
                        ref(),
                        """INSERT INTO attributes(attribute_id, version, created, updated, name, description)
                            VALUES (:attributeId, :version, :created, :updated, :name, :description)""",
                        mapOf(
                                "attributeId" to ColumnDetails(
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
                                        default = { "Test Attribute" }
                                ),
                                "description" to ColumnDetails(
                                        default = { "Attribute Description "}
                                )
                        )
                )
            }
        }.initialize(context)
    }
}
