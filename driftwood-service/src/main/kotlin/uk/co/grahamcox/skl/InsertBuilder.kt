package uk.co.grahamcox.skl

/**
 * Builder for building an INSERT statement
 */
class InsertBuilder(private val table: String) : QueryBuilder {

    /** The map of bind values */
    private val binds = mutableMapOf<String, Any?>()

    /** The list of fields to set values on */
    private val fields = mutableListOf<Pair<String, Any>>()

    /** The list of values to return */
    private val returning = mutableListOf<String>()

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
     * Indicate that the given field is set to the given value
     * @param field The name of the field
     * @param value The value to use
     * @return this, for chaining
     */
    fun set(field: String, value: Any) : InsertBuilder {
        fields.add(Pair(field, value))
        return this
    }

    /**
     * Build a Bind parameter to use
     * @param value The value of the bind
     * @return the bind term
     */
    fun bind(value: Any?): BindTerm {
        val bindKey = "bv${binds.size}"
        binds[bindKey] = value

        return BindTerm(bindKey)
    }

    /**
     * Cast the given term to the given type
     * @param term The term to cast
     * @param castTo The type to cast to
     * @return the cast term
     */
    fun cast(term: Any, castTo: String) = CastTerm(term, castTo)

    /**
     * Indicate that the entire new row should be returned
     * @return this, for chaining
     */
    fun returnAll() : InsertBuilder {
        returning.add("*")
        return this
    }

    /**
     * Indicate that some fields should be returned
     * @return this, for chaining
     */
    fun returns(vararg field: String) : InsertBuilder {
        returning.addAll(field)
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
