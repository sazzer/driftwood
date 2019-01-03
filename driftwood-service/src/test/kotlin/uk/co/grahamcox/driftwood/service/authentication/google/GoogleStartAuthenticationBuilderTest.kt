package uk.co.grahamcox.driftwood.service.authentication.google

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

/**
 * Unit tests for the GoogleStartAuthenticationBuilder
 */
internal class GoogleStartAuthenticationBuilderTest {
    /**
     * Test the builder
     */
    @Test
    fun test() {
        val testSubject = GoogleStartAuthenticationBuilder(
                authUrl = URI("https://accounts.google.com/o/oauth2/v2/auth"),
                clientId = "484625354845-d8v3pkn5corneerhbh4c03dg42nvj360.apps.googleusercontent.com",
                redirectUriBuilder = { URI("http://localhost:8080/api/authentication/external/google/callback") },
                nonceBuilder = { "7C262CE9-C730-4BED-9F05-EA2CE0E51C27" }
        )

        val result = testSubject.startAuthentication()

        Assertions.assertAll(
                Executable { Assertions.assertEquals("7C262CE9-C730-4BED-9F05-EA2CE0E51C27", result.state) },
                Executable { Assertions.assertEquals(
                        UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                                .queryParam("client_id", "484625354845-d8v3pkn5corneerhbh4c03dg42nvj360.apps.googleusercontent.com")
                                .queryParam("response_type", "code")
                                .queryParam("scope", "openid email profile")
                                .queryParam("redirect_uri", "http://localhost:8080/api/authentication/external/google/callback")
                                .queryParam("state", "7C262CE9-C730-4BED-9F05-EA2CE0E51C27")
                                .build()
                                .toUri(),
                        result.redirect) }
        )
    }
}
