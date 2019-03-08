package uk.co.grahamcox.driftwood.service.characters.attributes

/**
 * Exception to indicate that no attribute could be found with the given ID
 * @property id The ID of the attribute
 */
class AttributeNotFoundException(val id: AttributeId) : RuntimeException("Attribute not found: ${id.id}")
