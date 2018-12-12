package uk.co.grahamcox.driftwood.service.openid.rest

import java.lang.RuntimeException

/**
 * Exception to indicate that the Grant Type was unknown
 */
class UnknownGrantTypeException(grant: String) : OpenIDException(
        ErrorResponse(
                error = ErrorCode.UNSUPPORTED_GRANT_TYPE,
                description = "The grant type is not supported",
                details = mapOf("grant_type" to grant)
        )
)
