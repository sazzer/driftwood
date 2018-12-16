package uk.co.grahamcox.driftwood.service.acceptance.openid

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.util.CollectionUtils
import uk.co.grahamcox.driftwood.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.driftwood.service.acceptance.requester.Response
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import java.util.*

/**
 * Tests for OpenID Token requests using the Client Credentials Grant
 */
class ClientCredentialsAcceptanceTest : AcceptanceTestBase() {
    /** The means to seed user records */
    @Autowired
    private lateinit var userSeeder: DatabaseSeeder

    /** The means to seed client records */
    @Autowired
    private lateinit var clientSeeder: DatabaseSeeder

    /** The Client ID to use */
    private lateinit var clientId: String

    /** The Client Secret to use */
    private lateinit var clientSecret: String

    /**
     * Create initial test data
     */
    @BeforeEach
    fun createClient() {
        val user = userSeeder()
        val client = clientSeeder(
                "ownerId" to user["userId"]!!.toString(),
                "grantTypes" to "client_credentials"
        )

        clientId = client["clientId"]!!.toString()
        clientSecret = client["clientSecret"]!!.toString()
    }

    /**
     * Test when the Client details are not provided
     */
    @Test
    fun noScopes() {
        val response = makeRequest(clientId = clientId, clientSecret = clientSecret, params = mapOf(
                "grant_type" to "client_credentials"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                Executable { Assertions.assertEquals(3, response.getValue("size(body/*)")) },
                Executable { MatcherAssert.assertThat(response.getValue("body/access_token") as String,
                        Matchers.matchesPattern("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]+\$")) },
                Executable { Assertions.assertEquals("Bearer", response.getValue("body/token_type")) },

                Executable { MatcherAssert.assertThat(response.getValue("body/expires_in") as Int, Matchers.allOf(
                        Matchers.greaterThanOrEqualTo(9),
                        Matchers.lessThanOrEqualTo(11)
                )) }
        )
    }

    /**
     * Make a request to the Token endpoint
     */
    private fun makeRequest(params: Map<String, String> = emptyMap(),
                            clientId: String? = null,
                            clientSecret: String? = null): Response {
        if (clientId != null && clientSecret != null) {
            requester.setBasicAuth(clientId, clientSecret)
        }
        val body = params.mapValues { listOf(it.value) }

        return requester.post("/api/oauth2/token", CollectionUtils.toMultiValueMap(body))
    }
}
