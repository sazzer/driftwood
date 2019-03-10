package uk.co.grahamcox.driftwood.service.characters.attributes

import uk.co.grahamcox.driftwood.service.model.Page
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.model.SortDirection

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

    /**
     * Get a list of all the matching attributes
     * @param filters The filters to apply to the list
     * @param sorts The sorts to apply to the list
     * @param offset The offset of the first attribute
     * @param pageSize The page size to return
     */
    fun list(filters: AttributeFilters = AttributeFilters(),
             sorts: List<Pair<AttributeSortField, SortDirection>> = emptyList(),
             offset: Int = 0,
             pageSize: Int = 10) : Page<AttributeId, AttributeData>
}
