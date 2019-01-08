package uk.co.grahamcox.driftwood.service.authentication

import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.users.UserData
import uk.co.grahamcox.driftwood.service.users.UserId

/**
 * Interface describing how to authenticate a user after we are redirected back
 */
interface AuthenticatedUserLoader {
    /**
     * Authenticate a user
     * @param params The parameters passed back in from the authentication result
     * @param expectedState The expected state for the callback
     * @return the authenticated user details
     */
    fun authenticateUser(params: Map<String, String>, expectedState: String?): Resource<UserId, UserData>
}
