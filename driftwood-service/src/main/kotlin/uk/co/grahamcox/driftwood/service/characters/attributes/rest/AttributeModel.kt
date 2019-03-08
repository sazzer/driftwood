package uk.co.grahamcox.driftwood.service.characters.attributes.rest

/**
 * Data representing a Attribute on the API
 * @property name The name of the attribute
 * @property description The description of the attribute
 */
data class AttributeModel(
        val name: String,
        val description: String
)
