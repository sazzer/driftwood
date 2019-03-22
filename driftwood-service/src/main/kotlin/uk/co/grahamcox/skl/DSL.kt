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

/**
 * Mechanism to build a INSERT statement
 * @param table The table to insert into
 * @param builder The builder to use
 * @return the Insert Builder
 */
fun insert(table: String, builder: InsertBuilder.() -> Unit): InsertBuilder {
    val result = InsertBuilder(table)
    result.invoke(builder)
    return result
}
