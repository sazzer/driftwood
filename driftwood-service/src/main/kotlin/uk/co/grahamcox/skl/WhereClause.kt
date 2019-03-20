package uk.co.grahamcox.skl

/**
 * Representation of a WHERE Clause
 */
interface WhereClause {
    /**
     * Actually build the WHERE Clause
     * @return the built clause
     */
    fun build() : String
}
