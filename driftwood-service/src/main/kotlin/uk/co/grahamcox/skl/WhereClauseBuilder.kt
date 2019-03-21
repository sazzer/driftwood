package uk.co.grahamcox.skl

import java.lang.StringBuilder

/**
 * DSL Builder for constructing a WHERE Clause
 */
class WhereClauseBuilder(private val joinClause: String) {
    /** The list of Where Clauses we've built */
    private val whereClauses = mutableListOf<WhereClause>()

    /**
     * Invoke a lambda to allow the SELECT statement to be populated
     * @param builder The builder to use
     * @return this, for chaining
     */
    fun invoke(builder: WhereClauseBuilder.() -> Unit): WhereClauseBuilder {
        builder.invoke(this)
        return this
    }

    /**
     * Add a Where Clause for an equality check
     * @param left The left side of the clause
     * @param right The right side of the clause
     * @return this, for chaining
     */
    fun eq(left: Any, right: Any) = where(left, "=", right)

    /**
     * Add a Where Clause for an inequality check
     * @param left The left side of the clause
     * @param right The right side of the clause
     * @return this, for chaining
     */
    fun ne(left: Any, right: Any) = where(left, "!=", right)

    /**
     * Add a Where Clause for a null check
     * @param term The term to check
     * @reutrn this, for chaining
     */
    fun isNull(term: Any): WhereClauseBuilder {
        val clauseBuilder = StringBuilder()
        clauseBuilder.append(formatTerm(term))
        clauseBuilder.append(" IS NULL")

        whereClauses.add(SimpleWhereClause(clauseBuilder.toString()))

        return this
    }

    /**
     * Add a Where Clause for a not-null check
     * @param term The term to check
     * @reutrn this, for chaining
     */
    fun notNull(term: Any): WhereClauseBuilder {
        val clauseBuilder = StringBuilder()
        clauseBuilder.append(formatTerm(term))
        clauseBuilder.append(" IS NOT NULL")

        whereClauses.add(SimpleWhereClause(clauseBuilder.toString()))

        return this
    }

    /**
     * Generic where clause with two terms and an operator
     * @param left The left term
     * @param op The operator
     * @param right The right term
     * @return this, for chaining
     */
    fun where(left: Any, op: String, right: Any) : WhereClauseBuilder {
        val clauseBuilder = StringBuilder()
        clauseBuilder.append(formatTerm(left))
        clauseBuilder.append(" $op ")
        clauseBuilder.append(formatTerm(right))

        whereClauses.add(SimpleWhereClause(clauseBuilder.toString()))

        return this
    }

    /**
     * Add an AND group to the Where clause
     * @param builder The builder to use
     * @return this, for chaining
     */
    fun and(builder: WhereClauseBuilder.() -> Unit): WhereClauseBuilder {
        val whereClauseBuilder = WhereClauseBuilder("AND")
        builder(whereClauseBuilder)

        whereClauses.add(whereClauseBuilder.build())

        return this
    }

    /**
     * Add an AND group to the Where clause
     * @param builder The builder to use
     * @return this, for chaining
     */
    fun or(builder: WhereClauseBuilder.() -> Unit): WhereClauseBuilder {
        val whereClauseBuilder = WhereClauseBuilder("OR")
        builder(whereClauseBuilder)

        whereClauses.add(whereClauseBuilder.build())

        return this
    }

    /**
     * Build the actual WHERE Clause we are dealing with here
     * @return the built clause
     */
    fun build() : WhereClause {
        return JoiningWhereClause(whereClauses, joinClause)
    }
}
