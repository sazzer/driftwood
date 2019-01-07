package uk.co.grahamcox.driftwood.service.authentication.google

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.util.CollectionUtils
import org.springframework.web.client.RestTemplate
import java.net.URI

/**
 * Unit tests for the Google Access Token loader
 */
internal class GoogleAccessTokenLoaderTest {
    /** The rest template to use */
    private val restTemplate = RestTemplate()

    /** The mock server to use */
    private val mockServer = MockRestServiceServer.bindTo(restTemplate).build()

    /**
     * Test loading the Authentication Token for the given Auth Code
     */
    @Test
    fun testLoadToken() {
        val testSubject = GoogleAccessTokenLoader(
                tokenUrl = URI("https://www.googleapis.com/oauth2/v4/token"),
                clientId = "googleClientId",
                clientSecret = "googleClientSecret",
                redirectUriBuilder = { URI("http://localhost:8080/api/authentication/callback") },
                restTemplate = restTemplate
        )

        val response = """{
          "access_token": "googleAccessToken",
          "expires_in": 3600,
          "scope": "https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
          "token_type": "Bearer",
          "id_token": "googleIdToken"
        }"""

        mockServer.expect(MockRestRequestMatchers.requestTo("https://www.googleapis.com/oauth2/v4/token"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"))
                .andExpect(MockRestRequestMatchers.content().formData(
                        CollectionUtils.toMultiValueMap(mapOf(
                                "code" to listOf("googleAuthCode"),
                                "client_id" to listOf("googleClientId"),
                                "client_secret" to listOf("googleClientSecret"),
                                "grant_type" to listOf("authorization_code"),
                                "redirect_uri" to listOf("http://localhost:8080/api/authentication/callback")
                        ))
                ))
                .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_JSON))

        val token = testSubject.load("googleAuthCode")

        Assertions.assertAll(
                Executable { Assertions.assertEquals("googleAccessToken", token.accessToken) },
                Executable { Assertions.assertEquals("Bearer", token.tokenType) },
                Executable { Assertions.assertEquals(3600, token.expiresIn) },
                Executable { Assertions.assertEquals("googleIdToken", token.idToken) }
        )
    }
}
