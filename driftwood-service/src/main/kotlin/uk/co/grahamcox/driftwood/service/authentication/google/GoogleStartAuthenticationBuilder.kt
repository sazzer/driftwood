package uk.co.grahamcox.driftwood.service.authentication.google

import org.slf4j.LoggerFactory
import org.springframework.util.CollectionUtils
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.driftwood.service.authentication.StartAuthenticationBuilder
import uk.co.grahamcox.driftwood.service.authentication.StartAuthenticationResult
import java.net.URI

/**
 * Implementation of the Start Authentication Builder for working with Google
 */
class GoogleStartAuthenticationBuilder(
        private val authUrl: URI,
        private val clientId: String,
        private val redirectUriBuilder: () -> URI,
        private val nonceBuilder: () -> String
) : StartAuthenticationBuilder {
    companion object {
        /** The logger to use  */
        private val LOG = LoggerFactory.getLogger(GoogleStartAuthenticationBuilder::class.java)
    }

    /**
     * Build the details to start authenticating with an external provider
     * @return the redirect details
     */
    override fun startAuthentication(): StartAuthenticationResult {
        val nonce = nonceBuilder()
        val redirectUri = redirectUriBuilder()

        val params = mapOf(
                "client_id" to clientId,
                "response_type" to "code",
                "scope" to "openid email profile",
                "redirect_uri" to redirectUri.toString(),
                "state" to nonce
        ).mapValues { listOf(it.value) }

        val uri = UriComponentsBuilder.fromUri(authUrl)
                .queryParams(CollectionUtils.toMultiValueMap(params))
                .build()
                .toUri()

        val result = StartAuthenticationResult(uri, nonce)
        LOG.debug("Build authentication result: {}", result)
        return result

    }
}
