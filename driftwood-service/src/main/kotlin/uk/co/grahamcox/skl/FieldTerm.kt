package uk.co.grahamcox.skl


/**
 * Representation of a field term for a query
 * @property fieldName The name of the field
 * @property tableName The name of the table the field is from
 */
data class FieldTerm(val fieldName: String, val tableName: String? = null)
