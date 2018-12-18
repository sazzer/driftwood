package uk.co.grahamcox.driftwood.service.users.rest

/**
 * Data representing a User on the API
 * @property name The name of the user
 * @property email The email address of the user, if available
 * @property logins The user logins, if available
 */
data class UserModel(
        val name: String,
        val email: String?,
        val logins: List<UserLoginModel>?
)
