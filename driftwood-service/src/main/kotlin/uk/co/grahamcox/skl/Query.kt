package uk.co.grahamcox.skl

/**
 * Representation of a Query that can be executed
 * @property sql The SQL Query to execute
 * @property binds The bind values to use
 */
data class Query(
        val sql: String,
        val binds: Map<String, Any?>
)
