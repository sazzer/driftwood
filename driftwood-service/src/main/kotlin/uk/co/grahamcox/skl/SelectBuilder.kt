package uk.co.grahamcox.skl

/**
 * Builder for building a SELECT statement
 */
class SelectBuilder : QueryBuilder() {
    /** The list of table names to work with */
    private val tableNames = mutableListOf<String>()

    /** The list of fields to return */
    private val selectFields = mutableListOf<SelectField>()

    /** The list of where clauses to add */
    private val whereClauses = mutableListOf<WhereClause>()

    /** The offset to select from */
    private var offset: Int? = null

    /** The limit to select */
    private var limit: Int? = null

    /**
     * Invoke a lambda to allow the SELECT statement to be populated
     * @param builder The builder to use
     * @return this, for chaining
     */
    fun invoke(builder: SelectBuilder.() -> Unit): SelectBuilder {
        builder.invoke(this)
        return this
    }

    /**
     * Actually build the query that we want to execute
     * @return the build query
     */
    override fun build(): Query {
        val builder = StringBuilder()
        builder.append("SELECT ")
        if (selectFields.isNotEmpty()) {
            val formatted = selectFields.map { field ->
                if (field.alias.isNullOrBlank()) {
                    formatTerm(field.term)
                } else {
                    "${formatTerm(field.term)} AS ${field.alias}"
                }
            }
            builder.append(formatted.joinToString(", "))
        } else {
            builder.append("*")
        }
        builder.append(" FROM ")
        builder.append(tableNames.joinToString(", "))

        if (whereClauses.isNotEmpty()) {
            val totalWhereClause = whereClauses
                    .map(WhereClause::build)
                    .filterNot { it.isBlank() }
                    .joinToString(" AND ")
            if (!totalWhereClause.isNullOrBlank()) {
                builder.append(" WHERE ")
                builder.append(totalWhereClause)
            }
        }

        offset?.let { builder.append(" OFFSET $it") }
        limit?.let { builder.append(" LIMIT $it") }

        return Query(builder.toString(), binds)
    }

    /**
     * Specify a table to perform the SELECT from
     * @param tableName The name of the table
     * @return this, for chaining
     */
    fun from(vararg tableNames: String): SelectBuilder {
        this.tableNames.addAll(tableNames)
        return this
    }

    /**
     * Add a definition to return from the SELECT statement
     * @param term The term to return
     * @param alias An alias for the term
     * @return this, for chaining
     */
    fun returning(term: Any, alias: String? = null) : SelectBuilder {
        selectFields.add(SelectField(term, alias))
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
     * Set the offset to select from
     * @param value The value to use
     */
    fun offset(value: Int) {
        this.offset = value
    }

    /**
     * Set the limit to select by
     * @param value The value to use
     */
    fun limit(value: Int) {
        this.limit = value
    }
}
