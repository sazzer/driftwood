package uk.co.grahamcox.driftwood.service.authorization.authorizor

import uk.co.grahamcox.driftwood.service.users.UserId

/**
 * Context in which Authorization calls are made
 */
interface AuthorizationContext {
    /**
     * Check that the user logged in is the one requested
     * @param userId The User ID to check that we are the same as
     */
    fun sameUser(userId: UserId)
}
