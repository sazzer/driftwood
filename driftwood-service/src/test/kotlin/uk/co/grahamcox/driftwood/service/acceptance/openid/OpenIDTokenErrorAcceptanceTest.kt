package uk.co.grahamcox.driftwood.service.acceptance.openid

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
 * Tests for OpenID Token requests that result in errors
 */
class OpenIDTokenErrorAcceptanceTest : AcceptanceTestBase() {
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
                "ownerId" to user["userId"]!!.toString()
        )

        clientId = client["clientId"]!!.toString()
        clientSecret = client["clientSecret"]!!.toString()
    }

    /**
     * Test when the Client details are not provided
     */
    @Test
    fun noClient() {
        val response = makeRequest(params = mapOf("grant_type" to "client_credentials"))
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) },

                Executable { response.assertBody("""{
                    "error": "invalid_client",
                    "error_description": "Client details were missing or invalid"
                }""".trimMargin()) }
        )
    }

    /**
     * Test when the Client ID is not known
     */
    @Test
    fun invalidClientId() {
        val response = makeRequest(clientId = UUID.randomUUID().toString(),
                clientSecret = UUID.randomUUID().toString(),
                params = mapOf("grant_type" to "client_credentials"))
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) },

                Executable { response.assertBody("""{
                    "error": "invalid_client",
                    "error_description": "Client details were missing or invalid"
                }""".trimMargin()) }
        )
    }

    /**
     * Test when the Client Secret is not known
     */
    @Test
    fun invalidClientSecret() {
        val response = makeRequest(clientId = clientId,
                clientSecret = UUID.randomUUID().toString(),
                params = mapOf("grant_type" to "client_credentials"))
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode) },

                Executable { response.assertBody("""{
                    "error": "invalid_client",
                    "error_description": "Client details were missing or invalid"
                }""".trimMargin()) }
        )
    }

    /**
     * Test when the Grant Type is not provided
     */
    @Test
    fun noGrantType() {
        val response = makeRequest(clientId = clientId, clientSecret = clientSecret)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },

                Executable { response.assertBody("""{
                    "error": "invalid_request",
                    "error_description": "No grant type was specified"
                }""".trimMargin()) }
        )
    }

    /**
     * Test when the Grant Type provided is not supported
     */
    @Test
    fun unknownGrantType() {
        val response = makeRequest(clientId = clientId, clientSecret = clientSecret, params = mapOf(
                "grant_type" to "unsupported"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },

                Executable { response.assertBody("""{
                    "error": "unsupported_grant_type",
                    "error_description": "The grant type is not supported",
                    "grant_type": "unsupported"
                }""".trimMargin()) }
        )
    }

    /**
     * Test when the Scope provided is not supported
     */
    @Test
    fun unknownScope() {
        val response = makeRequest(clientId = clientId, clientSecret = clientSecret, params = mapOf(
                "grant_type" to "client_credentials",
                "scope" to "unknown"
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },

                Executable { response.assertBody("""{
                    "error": "invalid_scope",
                    "error_description": "The requested scopes were not all valid",
                    "scopes": [
                        "unknown"
                    ]
                }""".trimMargin()) }
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
