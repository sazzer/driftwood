package uk.co.grahamcox.driftwood.service.acceptance

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus

/**
 * Acceptance tests for the healthchecks
 */
class HealthAcceptanceTest : AcceptanceTestBase() {
    /**
     * Test getting the health of the system
     */
    @Test
    fun testHealth() {
        val response = requester.get("/actuator/health")

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                Executable { response.assertBody("""{"status": "UP"}""") }
        )
    }
}
