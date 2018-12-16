package uk.co.grahamcox.driftwood.service.openid.rest

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Representation of an Access Token on the API
 * @property accessToken The actual token
 * @property expiry The number of seconds until it expires
 */
data class AccessTokenModel(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("expires_in") val expiry: Long
) {
    /** The token type */
    @JsonProperty("token_type")
    val tokenType = "Bearer"
}
