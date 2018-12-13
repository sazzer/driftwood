package uk.co.grahamcox.driftwood.service.acceptance.requester

import org.slf4j.LoggerFactory
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import java.util.*

/**
 * The mechanism to make HTTP requests to the server under test
 * @property restTemplate The REST Template to use
 */
class Requester(
        private val restTemplate: TestRestTemplate
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(Requester::class.java)
    }

    /** The last response from the server */
    private var lastResponseEntity: ResponseEntity<out Any>? = null

    /** The Authorization to use from the server */
    private var authorization: String? = null

    /** Get the last response in a safe manner */
    val lastResponse: Response
        get() = lastResponseEntity?.let(::Response)
                ?: throw IllegalStateException("No request has been recorded")

    /**
     * Reset the last response
     */
    fun reset() {
        lastResponseEntity = null
        authorization = null
    }

    /**
     * Make a GET request to the given URI
     * @param uri The URI
     * @return the last response
     */
    fun get(uri: String) = makeRequest(uri, HttpMethod.GET, null)

    /**
     * Make a POST request to the given URI
     * @param uri The URI to make a POST to
     * @param body The body to post
     * @return the last response
     */
    fun post(uri: String, body: Any) = makeRequest(uri, HttpMethod.POST, body)
    /**
     * Make a PUT request to the given URI
     * @param uri The URI to make a PUT to
     * @param body The body to put
     * @return the last response
     */
    fun put(uri: String, body: Any) = makeRequest(uri, HttpMethod.PUT, body)

    /**
     * Make a DELETE request to the given URI
     * @param uri The URI
     * @return the last response
     */
    fun delete(uri: String) = makeRequest(uri, HttpMethod.DELETE, null)

    /**
     * Actually make a request and retrive the response
     * @param uri The URI to make the request to
     * @param method The HTTP Method to use for the request
     * @param body The body to send, if any
     */
    fun makeRequest(uri: String, method: HttpMethod, body: Any?, expectedResponseType: Class<*> = Map::class.java): Response {
        lastResponseEntity = restTemplate.exchange(
                uri,
                method,
                HttpEntity(body, buildHeaders()),
                expectedResponseType)

        LOG.debug("Request to {} {} with payload {} returned response {}",
                method, uri, body, lastResponseEntity)
        return lastResponse
    }

    /**
     * Set the authorization header for Basic auth
     * @param username The username to use
     * @param password The password to use
     */
    fun setBasicAuth(username: String, password: String) {
        val encoded = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
        authorization = "Basic $encoded"
    }

    /**
     * Helper to build the headers needed for this request
     * @return the HTTP Headers
     */
    private fun buildHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        authorization?.let { authorization -> headers.set("Authorization", authorization) }
        return headers
    }
}
