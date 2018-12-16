package uk.co.grahamcox.driftwood.service.openid.token

/**
 * Exception to indicate that an access token was invalid
 */
class InvalidAccessTokenException(message: String?) : RuntimeException(message)
