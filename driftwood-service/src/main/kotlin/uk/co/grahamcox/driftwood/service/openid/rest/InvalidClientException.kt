package uk.co.grahamcox.driftwood.service.openid.rest

/**
 * Exception to indicate that the OpenID Client details were not valid
 */
class InvalidClientException : OpenIDException(ErrorResponse(
        error = ErrorCode.INVALID_CLIENT,
        description = "Client details were missing or invalid"
))
