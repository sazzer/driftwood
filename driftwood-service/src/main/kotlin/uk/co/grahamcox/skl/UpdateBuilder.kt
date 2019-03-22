package uk.co.grahamcox.skl

/**
 * Builder for building an UPDATE statement
 */
class UpdateBuilder(private val table: String) : MutatingQueryBuilder() {

    /** The list of where clauses to add */
    private val whereClauses = mutableListOf<WhereClause>()

    /**
     * Invoke a lambda to allow the UPDATE statement to be populated
     * @param builder The builder to use
     * @return this, for chaining
     */
    fun invoke(builder: UpdateBuilder.() -> Unit): UpdateBuilder {
        builder.invoke(this)
        return this
    }

    /**
     * Build a WHERE Clause
     * @param builder The builder to use
     */
    fun where(builder: WhereClauseBuilder.() -> Unit) {
        val whereClauseBuilder = WhereClauseBuilder("AND")
        builder(whereClauseBuilder)

        whereClauses.add(whereClauseBuilder.build())
    }

    /**
     * Actually build the query that we want to execute
     * @return the build query
     */
    override fun build(): Query {
        val builder = StringBuilder()
        builder.append("UPDATE ").append(table)

        builder.append(" SET ")

        val updates = fields
                .map { Pair(it.first, formatTerm(it.second)) }
                .map { "${it.first} = ${it.second}" }
        builder.append(updates.joinToString(", "))

        if (whereClauses.isNotEmpty()) {
            val totalWhereClause = whereClauses
                    .map(WhereClause::build)
                    .joinToString(" AND ")
            builder.append(" WHERE ")
            builder.append(totalWhereClause)
        }

        if (returning.isNotEmpty()) {
            builder.append(" RETURNING ")
            builder.append(returning.joinToString(", "))
        }

        return Query(builder.toString(), binds)
    }
}
