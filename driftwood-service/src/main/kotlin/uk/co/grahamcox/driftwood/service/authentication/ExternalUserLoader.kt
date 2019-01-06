package uk.co.grahamcox.driftwood.service.authentication

/**
 * Interface describing how to load the details of an external user after authentication
 */
interface ExternalUserLoader {
    /**
     * Load the external user details
     * @param params The parameters passed back in from the authentication result
     * @param expectedState The expected state for the callback
     * @return the external user details
     */
    fun loadExternalUser(params: Map<String, String>, expectedState: String?) : ExternalUser
}
