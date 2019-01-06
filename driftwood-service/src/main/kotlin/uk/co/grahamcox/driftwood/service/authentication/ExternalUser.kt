package uk.co.grahamcox.driftwood.service.authentication

/**
 * The details of an externally authenticated user
 * @property id The ID of the user in the external system
 * @property displayName The display name of the user
 * @property name The name of the user
 * @property email The email address of the user
 */
data class ExternalUser(
        val id: String,
        val displayName: String,
        val name: String,
        val email: String?
)
