package uk.co.grahamcox.driftwood.service.users.rest

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.co.grahamcox.driftwood.service.authorization.authorizor.AuthorizationSuccessResult
import uk.co.grahamcox.driftwood.service.authorization.authorizor.Authorizer
import uk.co.grahamcox.driftwood.service.users.UserId
import uk.co.grahamcox.driftwood.service.users.UserRetriever
import java.util.*

/**
 * Controller class for working with users
 */
@RestController
@RequestMapping("/api/users")
class UserController(private val userRetriever: UserRetriever) {
    /**
     * Get a User by the unique ID
     * @param id The ID of the user
     * @return the user details
     */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getUserById(@PathVariable("id") id: UUID,
                    authorizer: Authorizer) : IdentifiedUserModel {
        val userId = UserId(id)

        val authorization = authorizer {
            sameUser(userId)
        }

        val user = userRetriever.getById(userId)

        return IdentifiedUserModel(
                id = user.identity.id.id.toString(),
                user = UserModel(
                        name = user.data.name,
                        email = when (authorization) {
                            is AuthorizationSuccessResult -> user.data.email
                            else -> null
                        },
                        logins = when (authorization) {
                            is AuthorizationSuccessResult ->
                                user.data.logins.map { login ->
                                    UserLoginModel(
                                            provider = login.provider,
                                            providerId = login.providerId,
                                            displayName = login.displayName
                                    )
                                }.sortedWith(compareBy(UserLoginModel::provider, UserLoginModel::displayName))
                            else -> null
                        }
                )
        )
    }
}
