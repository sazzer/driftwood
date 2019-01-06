package uk.co.grahamcox.driftwood.service.authentication.google

import uk.co.grahamcox.driftwood.service.authentication.ExternalUser
import uk.co.grahamcox.driftwood.service.authentication.ExternalUserLoader

/**
 * Means to load the external user details after authenticating with Google
 */
class GoogleUserLoader : ExternalUserLoader {
    /**
     * Load the external user details
     * @param params The parameters passed back in from the authentication result
     * @param expectedState The expected state for the callback
     * @return the external user details
     */
    override fun loadExternalUser(params: Map<String, String>, expectedState: String?): ExternalUser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
