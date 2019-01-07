package uk.co.grahamcox.driftwood.service.authentication.google

import io.fusionauth.jwt.domain.JWT
import org.slf4j.LoggerFactory
import uk.co.grahamcox.driftwood.service.authentication.ExternalUser
import uk.co.grahamcox.driftwood.service.authentication.ExternalUserLoader
import java.lang.IllegalStateException

/**
 * Means to load the external user details after authenticating with Google
 */
class GoogleUserLoader(
        private val accessTokenLoader: GoogleAccessTokenLoader
) : ExternalUserLoader {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(GoogleUserLoader::class.java)
    }

    /**
     * Load the external user details
     * @param params The parameters passed back in from the authentication result
     * @param expectedState The expected state for the callback
     * @return the external user details
     */
    override fun loadExternalUser(params: Map<String, String>, expectedState: String?): ExternalUser {
        val code = params["code"] ?: throw IllegalStateException()

        val accessToken = accessTokenLoader.load(code)
        LOG.debug("Loaded the access token for code {}: {}", code, accessToken)

        val idToken = JWT.getDecoder().decode(accessToken.idToken, NoopJwtVerifier)
        LOG.debug("ID Token Claims for code {}: {}", code, idToken.allClaims)

        val user = ExternalUser(
                id = idToken.subject,
                name = idToken.getString("name"),
                email = idToken.getString("email"),
                displayName = idToken.getString("email")
        )

        LOG.debug("Loaded external user for code {}: {}", code, user)
        return user
    }
}
