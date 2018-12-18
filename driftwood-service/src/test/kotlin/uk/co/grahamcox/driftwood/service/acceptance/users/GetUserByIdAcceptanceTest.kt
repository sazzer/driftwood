package uk.co.grahamcox.driftwood.service.acceptance.users

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.util.CollectionUtils
import uk.co.grahamcox.driftwood.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import java.util.*

/**
 * Acceptance Tests for getting users by ID
 */
class GetUserByIdAcceptanceTest : AcceptanceTestBase() {
    /** The means to seed user records */
    @Autowired
    private lateinit var userSeeder: DatabaseSeeder

    /** The means to seed client records */
    @Autowired
    private lateinit var clientSeeder: DatabaseSeeder

    /**
     * Test getting the user that we are logged in as
     */
    @Test
    fun getMyUser() {
        val userId = UUID.randomUUID().toString()
        val version = UUID.randomUUID().toString()

        userSeeder(
                "userId" to userId,
                "version" to version,
                "created" to "2018-12-06T16:31:00Z",
                "updated" to "2018-12-06T16:31:00Z",
                "name" to "Graham",
                "email" to "graham@example.com",
                "logins" to "twitter,@graham,@graham;google,123456,graham@example.com"

        )
        val token = getTokenForUser(userId)
        requester.setAccessToken(token)

        val userResponse = requester.get("/api/users/$userId")
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, userResponse.statusCode) },

                Executable { userResponse.assertBody("""{
                    "id": "$userId",
                    "name": "Graham",
                    "email": "graham@example.com",
                    "logins": [
                        {
                            "provider": "google",
                            "providerId": "123456",
                            "displayName": "graham@example.com"
                        }, {
                            "provider": "twitter",
                            "providerId": "@graham",
                            "displayName": "@graham"
                        }
                    ]
                }""".trimMargin()) }
        )

    }

    /**
     * Helper to get an Access Token for a given User ID
     * @param userId The ID of the User
     * @return the Access Token
     */
    private fun getTokenForUser(userId: String): String {
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
        return token
    }
}
