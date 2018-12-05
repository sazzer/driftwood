package uk.co.grahamcox.driftwood.service.acceptance

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus

/**
 * Acceptance tests for the API Version endpoint
 */
class VersionAcceptanceTest : AcceptanceTestBase() {
    /**
     * Test getting the version of the API
     */
    @Test
    fun testHealth() {
        val response = requester.get("/api/version")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                Executable { response.assertBody("""{"version":"1.0.0"}""") }
        )
    }
}
