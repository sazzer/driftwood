package uk.co.grahamcox.driftwood.service.acceptance.openid

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.util.CollectionUtils
import uk.co.grahamcox.driftwood.service.acceptance.AcceptanceTestBase
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
     * Test when the Grant Type is not provided
     */
    @Test
    fun noGrantType() {
        requester.setBasicAuth(clientId, clientSecret)
        val body = emptyMap<String, String>()
                .mapValues { listOf(it.value) }

        val response = requester.post("/api/oauth2/token", CollectionUtils.toMultiValueMap(body))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },

                Executable { Assertions.assertEquals("invalid_request", response.getValue("/body/error")) }
        )

    }

    /**
     * Test when the Grant Type provided is not supported
     */
    @Test
    fun unknownGrantType() {
        requester.setBasicAuth(clientId, clientSecret)
        val body = mapOf(
                "grant_type" to "unsupported"
        ).mapValues { listOf(it.value) }

        val response = requester.post("/api/oauth2/token", CollectionUtils.toMultiValueMap(body))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode) },

                Executable { Assertions.assertEquals("unsupported_grant_type", response.getValue("/body/error")) },
                Executable { Assertions.assertEquals("unsupported", response.getValue("/body/grant_type")) }
        )

    }
}
