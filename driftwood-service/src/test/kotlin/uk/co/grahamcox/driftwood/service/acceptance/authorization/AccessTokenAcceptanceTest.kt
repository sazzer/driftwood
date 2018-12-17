package uk.co.grahamcox.driftwood.service.acceptance.authorization

import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.util.CollectionUtils
import uk.co.grahamcox.driftwood.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import java.util.*

/**
 * Acceptance Tests for working with access tokens
 */
class AccessTokenAcceptanceTest: AcceptanceTestBase() {
    /** The means to seed user records */
    @Autowired
    private lateinit var userSeeder: DatabaseSeeder

    /** The means to seed client records */
    @Autowired
    private lateinit var clientSeeder: DatabaseSeeder

    /** The User ID to work with */
    private val userId = UUID.randomUUID().toString()

    /** The access token to use */
    private lateinit var token: String

    /**
     * Create initial test data
     */
    @BeforeEach
    fun createClient() {
        userSeeder(
                "userId" to userId
        )
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

        token = accessTokenResponse.getValue("body/access_token") as String
        requester.reset()
    }

    /**
     * Test getting the User ID when a valid token is present
     */
    @TestFactory
    fun getUserIdPresent(): List<DynamicTest> {
        return listOf(
                "/api/authorization/debug/userId/required",
                "/api/authorization/debug/userId/optional"
        ).map { url -> DynamicTest.dynamicTest(url) {
            requester.setAccessToken(token)

            val response = requester.get(url)

            Assertions.assertAll(
                    Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                    Executable { response.assertBody("""{"id": "$userId"}""") }
            )
        }
        }
    }

    /**
     * Test getting the Access Token when a valid token is present
     */
    @TestFactory
    fun getAccessTokenPresent(): List<DynamicTest> {
        return listOf(
                "/api/authorization/debug/accessToken/required",
                "/api/authorization/debug/accessToken/optional"
        ).map { url -> DynamicTest.dynamicTest(url) {
            requester.setAccessToken(token)

            val response = requester.get(url)

            Assertions.assertAll(
                    Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                    Executable { Assertions.assertEquals(userId, response.getValue("body/user/id")) }
            )
        }
        }
    }
}
