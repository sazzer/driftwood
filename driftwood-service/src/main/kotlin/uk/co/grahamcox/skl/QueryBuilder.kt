package uk.co.grahamcox.skl

interface QueryBuilder {
    /**
     * Actually build the query that we want to execute
     * @return the build query
     */
    fun build(): Query
}
