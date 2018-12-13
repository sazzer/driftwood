package uk.co.grahamcox.driftwood.service.openid.rest

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Enumeration of the possible error codes
 */
enum class ErrorCode(@JsonValue val errorCode: String) {
    INVALID_REQUEST("invalid_request"),
    INVALID_CLIENT("invalid_client"),
    INVALID_GRANT("invalid_grant"),
    UNAUTHORIZED_CLIENT("unauthorized_client"),
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_type"),
    INVALID_SCOPE("invalid_scope")
}
