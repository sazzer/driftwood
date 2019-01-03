package uk.co.grahamcox.driftwood.service.authentication

import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

/**
 * Mechanism to generate a Redirect URI for callback when completing authentication
 */
class RedirectUriBuilder(private val service: String) : () -> URI {
    /**
     * Generate the Redirect URI
     * @return the Redirect URI
     */
    override operator fun invoke() = ServletUriComponentsBuilder.fromCurrentRequest()
            .replacePath("/api/authentication/external")
            .pathSegment(service)
            .pathSegment("callback")
            .replaceQuery(null)
            .build()
            .toUri()
}
