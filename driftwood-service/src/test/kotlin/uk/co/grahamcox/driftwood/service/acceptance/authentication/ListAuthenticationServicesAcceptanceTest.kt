package uk.co.grahamcox.driftwood.service.acceptance.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import uk.co.grahamcox.driftwood.service.acceptance.AcceptanceTestBase

/**
 * Acceptance Test for listing the possible authentication services
 */
class ListAuthenticationServicesAcceptanceTest : AcceptanceTestBase() {
    /**
     * Test getting the possible authentication services
     */
    @Test
    fun testListAuthenticationServices() {
        val response = requester.makeRequest(uri = "/api/authentication/external",
                method = HttpMethod.GET,
                expectedResponseType = List::class.java)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, response.statusCode) },

                Executable { response.assertBody("""["google"]""") }
        )
    }

}
