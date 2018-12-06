package uk.co.grahamcox.driftwood.service.users

import java.lang.RuntimeException

/**
 * Exception to indicate that no user could be found with the given ID
 * @property id The ID of the user
 */
class UserNotFoundException(val id: UserId) : RuntimeException("User not found: ${id.id}")
