package uk.co.grahamcox.driftwood.service.users.rest

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
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
    fun getUserById(@PathVariable("id") id: UUID) : IdentifiedUserModel {
        val user = userRetriever.getById(UserId(id))

        return IdentifiedUserModel(
                id = user.identity.id.id.toString(),
                user = UserModel(
                        name = user.data.name,
                        email = user.data.email,
                        logins = user.data.logins.map { login ->
                            UserLoginModel(
                                    provider = login.provider,
                                    providerId = login.providerId,
                                    displayName = login.displayName
                            )
                        }.sortedWith(compareBy(UserLoginModel::provider, UserLoginModel::displayName))
                )
        )
    }
}
