package uk.co.grahamcox.driftwood.service.authentication

/**
 * Authenticator that works in terms of simpler components
 */
class StrategyAuthenticator(
        private val startAuthenticationBuilder: StartAuthenticationBuilder
) : Authenticator {
    /**
     * Build the details to start authenticating with an external provider
     * @return the redirect details
     */
    override fun startAuthentication() = startAuthenticationBuilder.startAuthentication()
}
