package uk.co.grahamcox.driftwood.service.model

/**
 * Interface describing an ID of some resource
 */
interface Id<out T> {
    /** The value of the ID */
    val id: T
}
