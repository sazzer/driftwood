package uk.co.grahamcox.driftwood.service.model

import java.time.Instant
import java.util.*

/**
 * The identity of some resource
 * @property id The ID value
 * @property version The version of the resource
 * @property created When the resource was created
 * @property updated When the resource was updated
 */
data class Identity<out ID : Id<*>>(
        val id: ID,
        val version: UUID,
        val created: Instant,
        val updated: Instant
)
