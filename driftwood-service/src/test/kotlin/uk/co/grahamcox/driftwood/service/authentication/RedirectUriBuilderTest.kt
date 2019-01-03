package uk.co.grahamcox.driftwood.service.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.driftwood.service.CurrentRequest
import java.net.URI

/**
 * Unit tests for the Redirect URI Builder
 */
internal class RedirectUriBuilderTest {
    /** The test subject */
    private val testSubject = RedirectUriBuilder("google")

    /**
     * Test building a simple redirect URI with nothing special going on
     */
    @Test
    @CurrentRequest("http://example.com/api/authentication/external/google/start")
    fun testSimple() {
        val redirect = testSubject()

        Assertions.assertEquals(URI("http://example.com/api/authentication/external/google/callback"), redirect)
    }

    /**
     * Test building a redirect URI with a port number
     */
    @Test
    @CurrentRequest("http://example.com:8080/api/authentication/external/google/start")
    fun testPort() {
        val redirect = testSubject()

        Assertions.assertEquals(URI("http://example.com:8080/api/authentication/external/google/callback"), redirect)
    }

    /**
     * Test building a simple redirect URI with a query string
     */
    @Test
    @CurrentRequest("http://example.com/api/authentication/external/google/start?a=b&c=d")
    fun testQuerystring() {
        val redirect = testSubject()

        Assertions.assertEquals(URI("http://example.com/api/authentication/external/google/callback"), redirect)
    }
}
