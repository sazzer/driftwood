package uk.co.grahamcox.driftwood.service.authentication

/**
 * Registry of Authenticator instances
 */
class AuthenticatorRegistry(
        private val authenticators: Map<String, Authenticator>
) {
    /** The list of supported authenticator names */
    val names = authenticators.keys.sorted()

    /**
     * Get the authenticator with the given name
     * @param name The name of the authenticator
     * @return the authenticator
     */
    operator fun get(name: String) = authenticators[name] ?: throw UnknownAuthenticatorException(name)
}
