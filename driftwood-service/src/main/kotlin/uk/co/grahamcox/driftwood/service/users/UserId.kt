package uk.co.grahamcox.driftwood.service.users

import uk.co.grahamcox.driftwood.service.model.Id
import java.util.*

/**
 * The ID of a User
 * @property id The ID of the user
 */
data class UserId(override val id: UUID): Id<UUID>
