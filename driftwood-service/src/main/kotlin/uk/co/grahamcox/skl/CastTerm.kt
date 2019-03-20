package uk.co.grahamcox.skl

/**
 * Term representing casting a different term to a different type
 * @property term The term to cast
 * @property castTo The type to cast to
 */
data class CastTerm(val term: Any, val castTo: String)
