package uk.co.grahamcox.driftwood.service.users

/**
 * Data representing a User
 * @property name The name of the user
 * @property email The email address of the user, if known
 * @property logins The login details of the user
 */
data class UserData(
        val name: String,
        val email: String?,
        val logins: Set<UserLoginData>
)
