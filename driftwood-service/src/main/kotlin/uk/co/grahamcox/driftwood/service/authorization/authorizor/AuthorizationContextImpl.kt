package uk.co.grahamcox.driftwood.service.authorization.authorizor

import uk.co.grahamcox.driftwood.service.openid.token.AccessToken
import uk.co.grahamcox.driftwood.service.users.UserId

/**
 * Implementation of the Authorization Context
 * @property accessToken The access token to authorize
 */
class AuthorizationContextImpl(private val accessToken: AccessToken?) : AuthorizationContext {
    /** The result of authorization */
    var result: AuthorizationResult = AuthorizationSuccessResult()

    /**
     * Check that the user logged in is the one requested
     * @param userId The User ID to check that we are the same as
     */
    override fun sameUser(userId: UserId) {
        if (userId != accessToken?.user) {
            reject()
        }
    }

    /**
     * Reject authorization for this user
     */
    private fun reject() {
        result = AuthorizationFailedResult()
    }
}
