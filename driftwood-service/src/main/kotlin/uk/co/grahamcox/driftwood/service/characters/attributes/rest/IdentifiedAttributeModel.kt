package uk.co.grahamcox.driftwood.service.characters.attributes.rest

import com.fasterxml.jackson.annotation.JsonUnwrapped

/**
 * Identified version of a Attribute Model
 * @property id The ID of the attribute
 * @property attribute The attribute data
 */
data class IdentifiedAttributeModel(
        val id: String,
        @JsonUnwrapped val attribute: AttributeModel
)
