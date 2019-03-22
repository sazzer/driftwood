package uk.co.grahamcox.skl

/**
 * Builder for building an INSERT statement
 */
class InsertBuilder(private val table: String) : MutatingQueryBuilder() {
    /**
     * Invoke a lambda to allow the INSERT statement to be populated
     * @param builder The builder to use
     * @return this, for chaining
     */
    fun invoke(builder: InsertBuilder.() -> Unit): InsertBuilder {
        builder.invoke(this)
        return this
    }

    /**
     * Actually build the query that we want to execute
     * @return the build query
     */
    override fun build(): Query {
        val builder = StringBuilder()
        builder.append("INSERT INTO ").append(table).append("(")

        builder.append(fields.joinToString(", ") { it.first })

        builder.append(") VALUES (")

        builder.append(fields.joinToString(", ") { formatTerm(it.second) })

        builder.append(")")

        if (returning.isNotEmpty()) {
            builder.append(" RETURNING ")
            builder.append(returning.joinToString(", "))
        }

        return Query(builder.toString(), binds)
    }
}
