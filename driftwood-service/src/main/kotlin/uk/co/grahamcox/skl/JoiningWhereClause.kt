package uk.co.grahamcox.skl

/**
 * Representation of a WHERE Clause that simply joins a number of other clauses together
 * @property clauses The clauses to join
 * @property join The mechanism to join them with
 */
data class JoiningWhereClause(val clauses: List<WhereClause>, val join: String) : WhereClause {
    /**
     * Actually build the WHERE Clause
     * @return the built clause
     */
    override fun build(): String {
        return if (clauses.isNotEmpty()) {
            clauses
                    .map(WhereClause::build)
                    .filterNot(String::isNullOrBlank)
                    .joinToString(" $join ", "(", ")")
        } else {
            ""
        }
    }
}
