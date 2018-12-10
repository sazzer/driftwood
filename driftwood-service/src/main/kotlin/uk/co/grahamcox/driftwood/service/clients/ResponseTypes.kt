package uk.co.grahamcox.driftwood.service.clients

/**
 * Representation of the different supported Response Types
 */
enum class ResponseTypes(val id: String) {
    CODE("code"),
    TOKEN("token"),
    ID_TOKEN("id_token");

    companion object {
        /**
         * Helper to get the value from the enum that has the given ID
         * @param value The value to look up
         * @return the matching value
         */
        fun getByValue(value: String) = values().find { it.id == value }
    }
}
