package uk.co.grahamcox.driftwood.service.characters.attributes

import uk.co.grahamcox.driftwood.service.model.Resource

/**
 * Interface representing a means to retrieve attribute data
 */
interface AttributeRetriever {
    /**
     * Get the attribute with the given ID
     * @param id The ID of the attribute
     * @return The attribute
     */
    fun getById(id: AttributeId): Resource<AttributeId, AttributeData>
}
