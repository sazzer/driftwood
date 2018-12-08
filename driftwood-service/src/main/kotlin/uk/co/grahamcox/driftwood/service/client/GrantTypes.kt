package uk.co.grahamcox.driftwood.service.client

/**
 * Representation of the different supported Grant Types
 */
enum class GrantTypes(val id: String) {
    AUTHORIZATION_CODE("authorization_code"),
    IMPLICIT("implicit"),
    REFRESH_TOKEN("refresh_token"),
    CLIENT_CREDENTIALS("client_credentials")
}
