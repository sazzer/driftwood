package uk.co.grahamcox.driftwood.e2e.database

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * Definition of the database field to seed
 */
data class DatabaseField<T>(
        val field: String,
        val defaultValue: (() -> T)? = null,
        val converter: ((String) -> T)? = null

)

/**
 * Mechanism to seed the database
 */
class DatabaseSeeder(private val jdbcTemplate: NamedParameterJdbcTemplate,
                     private val sql: String,
                     private val fields: Map<String, DatabaseField<*>>) {
    companion object {
        private val LOG = LoggerFactory.getLogger(DatabaseSeeder::class.java)
    }

    /**
     * Seed the database from the provided data
     */
    fun seed(data: Map<String, String>) {
        LOG.debug("Seeding with data: {}", data)

        val bindValues = fields.map { (key, field) ->
            val value = data[key]
            val converter = field.converter
            val defaulter = field.defaultValue

            val realValue = if (value != null) {
                if (converter != null) {
                    converter(value)
                } else {
                    value
                }
            } else if (defaulter != null) {
                defaulter()
            } else {
                null
            }

            Pair(field.field, realValue)
        }.toMap()

        jdbcTemplate.update(sql, bindValues)
    }
}
