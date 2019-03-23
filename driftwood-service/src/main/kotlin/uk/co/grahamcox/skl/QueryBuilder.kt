package uk.co.grahamcox.skl

import java.time.Instant
import java.util.*

/**
 * Base that all query builders inherit from
 */
abstract class QueryBuilder {

    /** The map of bind values */
    protected val binds = mutableMapOf<String, Any?>()

    /**
     * Actually build the query that we want to execute
     * @return the build query
     */
    abstract fun build(): Query

    /**
     * Build a Bind parameter to use
     * @param value The value of the bind
     * @return the bind term
     */
    fun bind(value: Any?): BindTerm {
        val bindKey = "bv${binds.size}"
        binds[bindKey] = when (value) {
            is Instant -> Date.from(value)
            else -> value
        }

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
     * Build a Field Term to use
     * @param tableName The name of the table
     * @param fieldName The name of the field
     * @return the field term
     */
    fun field(tableName: String?, fieldName: String) : FieldTerm {
        return FieldTerm(fieldName, tableName)
    }

    /**
     * Build a Field Term to use
     * @param fieldName The name of the field
     * @return the field term
     */
    fun field(fieldName: String) = field(null, fieldName)

    /**
     * Build a Function Term to use
     * @param function The function
     * @param term The term
     * @return the function term
     */
    fun function(function: String, term: Any) = FunctionTerm(function, term)
}
