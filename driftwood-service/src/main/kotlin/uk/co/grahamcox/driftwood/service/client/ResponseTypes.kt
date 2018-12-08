package uk.co.grahamcox.driftwood.service.client

/**
 * Representation of the different supported Response Types
 */
enum class ResponseTypes(val id: String) {
    CODE("code"),
    TOKEN("token"),
    ID_TOKEN("id_token")
}
