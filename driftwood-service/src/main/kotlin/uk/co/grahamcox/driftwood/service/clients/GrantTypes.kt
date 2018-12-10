package uk.co.grahamcox.driftwood.service.clients

/**
 * Representation of the different supported Grant Types
 */
enum class GrantTypes(val id: String) {
    AUTHORIZATION_CODE("authorization_code"),
    IMPLICIT("implicit"),
    REFRESH_TOKEN("refresh_token"),
    CLIENT_CREDENTIALS("client_credentials");

    companion object {
        /**
         * Helper to get the value from the enum that has the given ID
         * @param value The value to look up
         * @return the matching value
         */
        fun getByValue(value: String) = values().find { it.id == value }
    }
}
