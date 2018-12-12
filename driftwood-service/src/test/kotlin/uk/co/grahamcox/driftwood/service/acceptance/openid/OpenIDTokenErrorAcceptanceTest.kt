package uk.co.grahamcox.driftwood.service.acceptance.openid

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.util.CollectionUtils
import uk.co.grahamcox.driftwood.service.acceptance.AcceptanceTestBase

/**
 * Tests for OpenID Token requests that result in errors
 */
class OpenIDTokenErrorAcceptanceTest : AcceptanceTestBase() {
    /**
     * Test when the Grant Type is not provided
     */
    @Test
    fun noGrantType() {
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
