package uk.co.grahamcox.driftwood.service.openid.rest

/**
 * Exception to indicate that the Grant Type was unknown
 */
class UnsupportedGrantTypeException(grant: String) : OpenIDException(
        ErrorResponse(
                error = ErrorCode.UNSUPPORTED_GRANT_TYPE,
                description = "The grant type is not supported by this client",
                details = mapOf("grant_type" to grant)
        )
)
