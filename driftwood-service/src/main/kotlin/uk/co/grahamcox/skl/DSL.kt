package uk.co.grahamcox.skl

/**
 * Mechanism to build a SELECT statement
 * @param builder The builder to use
 * @return the Select Builder
 */
fun select(builder: SelectBuilder.() -> Unit): SelectBuilder {
    val result = SelectBuilder()
    result.invoke(builder)
    return result
}
