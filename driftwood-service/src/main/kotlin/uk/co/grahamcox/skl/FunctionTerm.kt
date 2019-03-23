package uk.co.grahamcox.skl

/**
 * Term that represents wrapping another term in a function
 * @property function The function to use
 * @property term The term to wrap
 */
data class FunctionTerm(val function: String,
                        val term: Any)
