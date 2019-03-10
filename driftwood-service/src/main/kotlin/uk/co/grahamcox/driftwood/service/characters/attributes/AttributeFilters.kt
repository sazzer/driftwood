package uk.co.grahamcox.driftwood.service.characters.attributes

import java.time.Instant

/**
 * Filters to apply when listing attributes
 * @property name The name must match this
 * @property createdBefore The created date must be before this
 * @property createdAfter The created date must be after this
 * @property updatedBefore The updated date must be before this
 * @property updatedAfter The updated date must be after this
 */
data class AttributeFilters(
        val name: String? = null,
        val createdBefore: Instant? = null,
        val createdAfter: Instant? = null,
        val updatedBefore: Instant? = null,
        val updatedAfter: Instant? = null
)
