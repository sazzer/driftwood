package uk.co.grahamcox.driftwood.service.openid.rest

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

/**
 * Representation of an error response from the OpenID Controllers
 * @property error The error code
 * @property description The error description
 * @property uri The error URI
 */
data class ErrorResponse(
        @JsonProperty("error") val error: ErrorCode,
        @JsonProperty("error_description") val description: String? = null,
        @JsonProperty("error_uri") val uri: URI? = null,
        @get:JsonAnyGetter val details: Map<String, Any> = mapOf()
)
