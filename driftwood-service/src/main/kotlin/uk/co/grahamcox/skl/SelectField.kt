package uk.co.grahamcox.skl

/**
 * Representation of a field to return from a Select
 * @property term The term to select
 * @property alias The optional alias for the field
 */
data class SelectField(
        val term: Any,
        val alias: String?
)
