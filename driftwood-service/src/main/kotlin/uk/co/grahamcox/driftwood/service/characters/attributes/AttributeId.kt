package uk.co.grahamcox.driftwood.service.characters.attributes

import uk.co.grahamcox.driftwood.service.model.Id
import java.util.*

/**
 * The ID of a User
 * @property id The ID of the user
 */
data class AttributeId(override val id: UUID): Id<UUID>
