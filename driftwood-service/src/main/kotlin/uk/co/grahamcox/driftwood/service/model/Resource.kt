package uk.co.grahamcox.driftwood.service.model

/**
 * Representation of some resource
 * @property identity The identity of the resource
 * @property data The data of the resource
 */
data class Resource<out ID : Id<*>, out DATA>(
        val identity: Identity<ID>,
        val data: DATA
)
