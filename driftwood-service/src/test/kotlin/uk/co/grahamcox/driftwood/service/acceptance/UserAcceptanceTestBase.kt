package uk.co.grahamcox.driftwood.service.acceptance

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.CollectionUtils
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder

/**
 * Base class for Acceptance Test classes that also work with Users or Authentication
 */
open class UserAcceptanceTestBase : AcceptanceTestBase() {
    /** The means to seed user records */
    @Autowired
    private lateinit var userSeeder: DatabaseSeeder

    /** The means to seed client records */
    @Autowired
    private lateinit var clientSeeder: DatabaseSeeder

    /**
     * Seed a user with the provided data
     * @param data The data to seed the user with
     * @return the details that were actually used
     */
    protected fun seedUser(vararg data: Pair<String, String>) = userSeeder(*data)

    /**
     * Helper to get an Access Token for a given User ID.
     * Note that this will seed a Client into the database for this user ID, make a Client Credentials Grant call to that
     * client to get a token and then use that token
     * @param userId The ID of the User
     * @return the Access Token
     */
    protected fun useTokenForUser(userId: String) {
        val client = clientSeeder(
                "ownerId" to userId,
                "grantTypes" to "client_credentials"
        )

        val clientId = client["clientId"]!!.toString()
        val clientSecret = client["clientSecret"]!!.toString()

        val body = mapOf("grant_type" to "client_credentials")
                .mapValues { listOf(it.value) }

        requester.setBasicAuth(clientId, clientSecret)
        val accessTokenResponse = requester.post("/api/oauth2/token", CollectionUtils.toMultiValueMap(body))

        val token = accessTokenResponse.getValue("body/access_token") as String
        requester.reset()
        requester.setAccessToken(token)
    }
}
