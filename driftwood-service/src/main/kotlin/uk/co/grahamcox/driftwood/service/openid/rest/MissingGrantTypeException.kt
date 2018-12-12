package uk.co.grahamcox.driftwood.service.openid.rest

/**
 * Exception to indicate that the Grant Type was missing
 */
class MissingGrantTypeException : OpenIDException(
        ErrorResponse(
                error = ErrorCode.INVALID_REQUEST,
                description = "No grant type was specified"
        )
)
