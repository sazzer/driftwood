package uk.co.grahamcox.driftwood.service.authentication

import java.net.URI

/**
 * Result indicating how to redirect the user to start authentication
 * @property redirect The URI to redirect to
 * @property state The state for this request, to stop forgery requests
 */
data class StartAuthenticationResult(
        val redirect: URI,
        val state: String
)
