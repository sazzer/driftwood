package uk.co.grahamcox.driftwood.service.authentication

/**
 * Exception to indicate that an unknown authenticator was requested
 */
class UnknownAuthenticatorException(val name: String) : RuntimeException("Unknown Authenticator requested: $name")
