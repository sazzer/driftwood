package uk.co.grahamcox.driftwood.service.authentication

/**
 * Interface describing how to start authentication with an external provider
 */
interface StartAuthenticationBuilder {
    /**
     * Build the details to start authenticating with an external provider
     * @return the redirect details
     */
    fun startAuthentication() : StartAuthenticationResult
}
