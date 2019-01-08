package uk.co.grahamcox.driftwood.service.authentication

/**
 * Authenticator that works in terms of simpler components
 */
class StrategyAuthenticator(
        private val startAuthenticationBuilder: StartAuthenticationBuilder,
        private val authenticatedUserLoader: AuthenticatedUserLoader
) : Authenticator {
    /**
     * Build the details to start authenticating with an external provider
     * @return the redirect details
     */
    override fun startAuthentication() = startAuthenticationBuilder.startAuthentication()

    /**
     * Authenticate a user
     * @param params The parameters passed back in from the authentication result
     * @param expectedState The expected state for the callback
     * @return the authenticated user details
     */
    override fun authenticateUser(params: Map<String, String>, expectedState: String?) =
            authenticatedUserLoader.authenticateUser(params, expectedState)
}
