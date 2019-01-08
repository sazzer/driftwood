package uk.co.grahamcox.driftwood.service.authentication

import org.slf4j.LoggerFactory
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.users.UserData
import uk.co.grahamcox.driftwood.service.users.UserId
import uk.co.grahamcox.driftwood.service.users.UserLoginData
import uk.co.grahamcox.driftwood.service.users.UserService

/**
 * Default implementation of the Authenticated User Loader
 */
class DefaultAuthenticatedUserLoader(
        private val provider: String,
        private val externalUserLoader: ExternalUserLoader,
        private val userService: UserService
) : AuthenticatedUserLoader {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(DefaultAuthenticatedUserLoader::class.java)
    }

    /**
     * Authenticate a user
     * @param params The parameters passed back in from the authentication result
     * @param expectedState The expected state for the callback
     * @return the authenticated user details
     */
    override fun authenticateUser(params: Map<String, String>, expectedState: String?): Resource<UserId, UserData> {
        val externalUser = externalUserLoader.loadExternalUser(params, expectedState)

        val user = userService.getByProviderId(provider, externalUser.id)

        val result = when (user) {
            null -> userService.save(UserData(
                    name = externalUser.name,
                    email = externalUser.email,
                    logins = setOf(
                            UserLoginData(
                                    provider = provider,
                                    providerId = externalUser.id,
                                    displayName = externalUser.displayName
                            )
                    )
            ))
            else -> user
        }

        LOG.debug("Authenticated user: {}", result)
        return result
    }
}
