package uk.co.grahamcox.driftwood.service.database

import java.lang.IllegalStateException
import java.sql.ResultSet

/**
 * Get a String Array from a ResultSet
 * @param name The name of the column to get
 * @return the contents, as a String Array
 */
fun ResultSet.getStringArray(name: String): List<String> {
    val array = this.getArray(name)

    val arrayContents = when (array.baseTypeName) {
        "text" -> array.array as Array<String>
        else -> throw IllegalStateException("Array is not of supported type: " + array.baseTypeName)
    }

    return arrayContents.asList()
}
