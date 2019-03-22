package uk.co.grahamcox.skl

/**
 * Base Query Builder for any query that mutates data
 */
abstract class MutatingQueryBuilder : QueryBuilder() {

    /** The list of fields to set values on */
    protected val fields = mutableListOf<Pair<String, Any>>()

    /** The list of values to return */
    protected val returning = mutableListOf<String>()

    /**
     * Indicate that the given field is set to the given value
     * @param field The name of the field
     * @param value The value to use
     */
    fun set(field: String, value: Any) {
        fields.add(Pair(field, value))
    }

    /**
     * Indicate that the entire new row should be returned
     */
    fun returnAll() {
        returning.add("*")
    }

    /**
     * Indicate that some fields should be returned
     */
    fun returns(vararg field: String) {
        returning.addAll(field)
    }
}
