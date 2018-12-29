package uk.co.grahamcox.driftwood.service.users.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.driftwood.service.authorization.authorizor.AuthorizationSuccessResult
import uk.co.grahamcox.driftwood.service.authorization.authorizor.Authorizer
import uk.co.grahamcox.driftwood.service.rest.problem.Problem
import uk.co.grahamcox.driftwood.service.users.UserId
import uk.co.grahamcox.driftwood.service.users.UserNotFoundException
import uk.co.grahamcox.driftwood.service.users.UserRetriever
import java.net.URI
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
     * @param authorizer The authorizer to use
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

    /**
     * Handle when the requested user was not found
     */
    @ExceptionHandler(UserNotFoundException::class)
    fun unknownUser() = Problem(
            type = URI("tag:2018,grahamcox.co.uk:users/problems/not-found"),
            title = "Requested user was not found",
            statusCode = HttpStatus.NOT_FOUND
    )
}
