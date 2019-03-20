package uk.co.grahamcox.skl

/**
 * Representation of a simple WHERE Clause
 */
data class SimpleWhereClause(val clause: String) : WhereClause {
    /**
     * Actually build the WHERE Clause
     * @return the built clause
     */
    override fun build(): String {
        return clause
    }
}
