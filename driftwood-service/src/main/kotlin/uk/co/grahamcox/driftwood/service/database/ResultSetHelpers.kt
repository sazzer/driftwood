package uk.co.grahamcox.driftwood.service.database

import java.sql.ResultSet
import java.util.*

/**
 * Helper to get a UUID from a String column
 * @param name The name of the column
 * @return the value as a UUID
 */
fun ResultSet.getUUID(name: String) : UUID = UUID.fromString(this.getString(name))
