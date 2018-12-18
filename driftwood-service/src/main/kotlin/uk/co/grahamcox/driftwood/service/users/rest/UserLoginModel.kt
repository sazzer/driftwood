package uk.co.grahamcox.driftwood.service.users.rest

/**
 * Data representing a single login for a user at a single provider
 * @property provider The name of the provider
 * @property providerId The ID of the user at this provider
 * @property displayName The display name of the user at this provider
 */
data class UserLoginModel(
        val provider: String,
        val providerId: String,
        val displayName: String
)
