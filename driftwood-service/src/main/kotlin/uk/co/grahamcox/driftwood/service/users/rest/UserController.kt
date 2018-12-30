package uk.co.grahamcox.driftwood.service.users.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uk.co.grahamcox.driftwood.service.authorization.authorizor.AuthorizationContext
import uk.co.grahamcox.driftwood.service.authorization.authorizor.AuthorizationSuccessResult
import uk.co.grahamcox.driftwood.service.authorization.authorizor.Authorizer
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.rest.problem.Problem
import uk.co.grahamcox.driftwood.service.users.*
import java.net.URI
import java.util.*

/**
 * Controller class for working with users
 */
@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
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

        val authorization = authorizer(authorizeRequest(userId)) is AuthorizationSuccessResult

        val user = userService.getById(userId)

        return buildUserResponse(user, authorization)
    }

    /**
     * Update a User by the unique ID
     * @param id The ID of the user
     * @return the user details
     */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.PUT])
    fun updateUser(@PathVariable("id") id: UUID,
                   @RequestBody input: UserModel,
                   authorizer: Authorizer) : IdentifiedUserModel {
        val userId = UserId(id)

        authorizer(authorizeRequest(userId))

        val user = userService.getById(userId)
        val updatedUser = user.copy(
                data = UserData(
                        name = input.name,
                        email = input.email,
                        logins = (input.logins ?: emptyList()).map { login ->
                            UserLoginData(
                                    provider = login.provider,
                                    providerId = login.providerId,
                                    displayName = login.displayName
                            )
                        }.toSet()
                )
        )
        val savedUser = userService.save(updatedUser)

        return buildUserResponse(savedUser, true)
    }

    /**
     * Authorization function to authorize access to this controller
     * @param userId The ID of the user being accessed
     * @return the Authorization function
     */
    private fun authorizeRequest(userId: UserId): AuthorizationContext.() -> Unit {
        return {
            sameUser(userId)
        }
    }

    /**
     * Build the REST API response representing a single user
     * @param user The user to build the response for
     * @param authorized Whether the request is authorized to see the full details
     * @return the REST API response
     */
    private fun buildUserResponse(user: Resource<UserId, UserData>, authorized: Boolean): IdentifiedUserModel {
        return IdentifiedUserModel(
                id = user.identity.id.id.toString(),
                user = UserModel(
                        name = user.data.name,
                        email = when (authorized) {
                            true -> user.data.email
                            else -> null
                        },
                        logins = when (authorized) {
                            true ->
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
