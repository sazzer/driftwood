package uk.co.grahamcox.driftwood.service.authentication.google

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Representation of the Access Token retrieved from Google
 * @property accessToken The raw access token
 * @property tokenType The type of token
 * @property expiresIn The expiry duration, in seconds
 * @property idToken The ID Token
 */
data class GoogleAccessToken(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("token_type") val tokenType: String,
        @JsonProperty("expires_in") val expiresIn: Int,
        @JsonProperty("id_token") val idToken: String?
)
