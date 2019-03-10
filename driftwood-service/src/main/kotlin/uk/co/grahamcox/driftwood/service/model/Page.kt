package uk.co.grahamcox.driftwood.service.model

/**
 * Representation of a page of resources
 * @property data The actual data on the page
 * @property offset The offset of this page
 * @property total The total number of records
 */
data class Page<out ID : Id<*>, out DATA>(
        val data: List<Resource<ID, DATA>>,
        val offset: Int,
        val total: Int
)
