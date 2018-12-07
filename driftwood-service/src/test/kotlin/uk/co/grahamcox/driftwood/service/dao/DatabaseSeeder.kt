package uk.co.grahamcox.driftwood.service.dao

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.lang.IllegalArgumentException

/**
 * Details of a column to be seeded
 * @property default A lambda to generate a default value for the column
 * @property converter A lambda to convert the column to the desired type
 */
data class ColumnDetails<T>(
        val default: (() -> T)? = null,
        val converter: ((String) -> T)? = null
)

/**
 * Mechanism by which we can seed database tabled with data
 * @property jdbcTemplate The JDBC Template to call the database with
 * @property sql The SQL to use for the Insert
 * @property columns The columns to populate
 */
class DatabaseSeeder(
        private val jdbcTemplate: NamedParameterJdbcTemplate,
        private val sql: String,
        private val columns: Map<String, ColumnDetails<*>>
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DatabaseSeeder::class.java)
    }

    /**
     * Actually seed the database
     * @param data The data to seed with
     */
    operator fun invoke(vararg data: Pair<String, String>): Map<String, Any?> {
        val unknownValues = data.filterNot { columns.containsKey(it.first) }
        if (unknownValues.isNotEmpty()) {
            throw IllegalArgumentException("Unexpected data values: $unknownValues")
        }

        val dataMap = data.toMap()
        val params = columns.map { (name, details) ->
            val value = dataMap[name]

            val realValue = when {
                value == null -> details.default?.invoke()
                details.converter != null -> details.converter.invoke(value)
                else -> value
            }

            name to realValue
        }.toMap()

        LOG.debug("Creating record using SQL {} and binds {}", sql, params)
        jdbcTemplate.update(sql, params)

        return params
    }
}
