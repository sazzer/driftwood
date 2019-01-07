package uk.co.grahamcox.driftwood.service.authentication.google

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI

/**
 * Mechanism to load the access token details from Google after authenticating a user
 */
class GoogleAccessTokenLoader(
        private val tokenUrl: URI,
        private val clientId: String,
        private val clientSecret: String,
        private val redirectUriBuilder: () -> URI,
        private val restTemplate: RestTemplate) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(GoogleAccessTokenLoader::class.java)
    }

    /**
     * Load the Access Token for this authorization code
     * @param code The code to load the token for
     * @return The access token
     */
    fun load(code: String) : GoogleAccessToken {
        LOG.debug("Retrieving access token for authorization code {}", code)

        val params = mapOf(
                "code" to code,
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "redirect_uri" to redirectUriBuilder().toString(),
                "grant_type" to "authorization_code"
        )
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val tokenResponse = restTemplate.exchange(
                RequestEntity(
                        LinkedMultiValueMap(params.mapValues { listOf(it.value) }),
                        requestHeaders,
                        HttpMethod.POST,
                        tokenUrl),
                GoogleAccessToken::class.java)

        LOG.debug("Received token response: {}", tokenResponse)
        return tokenResponse.body!!
    }

}
