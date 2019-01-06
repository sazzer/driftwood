package uk.co.grahamcox.driftwood.service.authentication

/**
 * Authenticator that works in terms of simpler components
 */
class StrategyAuthenticator(
        private val startAuthenticationBuilder: StartAuthenticationBuilder,
        private val externalUserLoader: ExternalUserLoader
) : Authenticator {
    /**
     * Build the details to start authenticating with an external provider
     * @return the redirect details
     */
    override fun startAuthentication() = startAuthenticationBuilder.startAuthentication()

    /**
     * Load the external user details
     * @param params The parameters passed back in from the authentication result
     * @param expectedState The expected state for the callback
     * @return the external user details
     */
    override fun loadExternalUser(params: Map<String, String>, expectedState: String?) =
            externalUserLoader.loadExternalUser(params, expectedState)
}
