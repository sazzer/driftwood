package uk.co.grahamcox.driftwood.service.acceptance.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.driftwood.service.acceptance.AcceptanceTestBase

/**
 * Acceptance Test for authenticating with Google
 */
class GoogleAuthenticationAcceptanceTest : AcceptanceTestBase() {
    /** The server port to use */
    @LocalServerPort
    private lateinit var serverPort: Integer
    
    /**
     * Test starting authentication with Google
     */
    @Test
    fun testStartAuthentication() {
        val response = requester.get("/api/authentication/external/google/start")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.FOUND, response.statusCode) },

                Executable { Assertions.assertNotNull(response.headers.location) },
                Executable { Assertions.assertNotNull(response.headers["set-cookie"]) },

                Executable { Assertions.assertNull(response.getValue("/body")) }
        )

        val redirectLocation = UriComponentsBuilder.fromUri(response.headers.location!!)
                .build()

        Assertions.assertAll(
                Executable { Assertions.assertEquals("https", redirectLocation.scheme) },

                Executable { Assertions.assertEquals("accounts.google.com", redirectLocation.host) },
                Executable { Assertions.assertEquals(-1, redirectLocation.port) },

                Executable { Assertions.assertEquals("/o/oauth2/v2/auth", redirectLocation.path) },

                Executable { Assertions.assertEquals(5, redirectLocation.queryParams.size) },
                Executable { Assertions.assertEquals(listOf("googleClientId"), redirectLocation.queryParams["client_id"]) },
                Executable { Assertions.assertEquals(listOf("code"), redirectLocation.queryParams["response_type"]) },
                Executable { Assertions.assertEquals(listOf("openid%20email%20profile"), redirectLocation.queryParams["scope"]) },
                Executable { Assertions.assertEquals(listOf("http://localhost:$serverPort/api/authentication/external/google/callback"), redirectLocation.queryParams["redirect_uri"]) },
                Executable { Assertions.assertNotNull(redirectLocation.queryParams["state"]) }
        )

        val state = redirectLocation.queryParams.get("state")?.get(0)!!

        val cookies = response.headers["set-cookie"]!!
        Assertions.assertAll(
                Executable { Assertions.assertEquals(2, cookies.size) },

                Executable { Assertions.assertTrue(cookies.contains("externalAuthenticationService=google")) },
                Executable { Assertions.assertTrue(cookies.contains("externalAuthenticationState=$state")) }
        )
    }

}
