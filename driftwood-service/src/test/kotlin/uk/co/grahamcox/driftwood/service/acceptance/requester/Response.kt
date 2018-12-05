package uk.co.grahamcox.driftwood.service.acceptance.requester

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.jxpath.JXPathContext
import org.junit.jupiter.api.Assertions
import org.springframework.http.ResponseEntity

/**
 * Response from the Requester that makes checking the contents easier
 * @property response The actual response
 */
data class Response(
        val response: ResponseEntity<out Any>
) {
    /** The HTTP Status Code returned */
    val statusCode = response.statusCode

    /** The HTTP Response Headers */
    val headers = response.headers

    /** The JXPath Context for interrogating the response */
    val context = JXPathContext.newContext(response)

    /**
     * Get the value from the response for the given JXPath
     * @param path The path to the response field
     */
    fun getValue(path: String) = context.getValue(path)

    /**
     * Assert that the body matches the expected JSON
     * @param expected The expected JSON
     */
    fun assertBody(expected: String) {
        val expectedBody = ObjectMapper().readValue(expected, Any::class.java)

        Assertions.assertEquals(expectedBody, response.body)
    }
}
