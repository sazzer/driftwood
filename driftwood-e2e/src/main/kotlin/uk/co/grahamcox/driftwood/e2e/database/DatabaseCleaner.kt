package uk.co.grahamcox.driftwood.e2e.database

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.JdbcUtils
import javax.sql.DataSource

/**
 * Mechanism to clean the database between tests
 */
class DatabaseCleaner(
        private val dataSource: DataSource,
        private val jdbcTemplate: JdbcTemplate
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DatabaseCleaner::class.java)

        /** The list of tables to ignore */
        private val ignore = listOf(
                "flyway_schema_history",

                // Users and Clients are handled specially
                "users",
                "clients"
        )
    }

    /**
     * Clean the database
     */
    fun clean() {
        LOG.info("Cleaning the database")

        val tables = listTables()
        if (tables.isNotEmpty()) {
            val sql = tables.joinToString(separator = ", ", prefix = "TRUNCATE ")
            jdbcTemplate.update(sql)
        }
        LOG.info("Truncated tables: {}", tables)

        jdbcTemplate.update("DELETE FROM users WHERE user_id NOT IN (SELECT owner_id FROM clients)")
    }

    /**
     * List all of the tables in the database to be cleaned
     * @return the tables
     */
    private fun listTables() : List<String> {
        val result = mutableListOf<String>()

        JdbcUtils.extractDatabaseMetaData(dataSource) { metadata ->
            val tables = metadata.getTables(metadata.userName, null, null, arrayOf("TABLE"))
            while (tables.next()) {
                val table = tables.getString(3)
                LOG.info("Considering table to truncate: {}", table)
                if (!ignore.contains(table.toLowerCase())) {
                    result.add(table)
                }
            }
        }

        return result
    }
}
