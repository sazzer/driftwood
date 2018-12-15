package uk.co.grahamcox.driftwood.service.openid.token

/**
 * Representation of a means to serialize an access token to a string and back
 */
interface AccessTokenSerializer {
    /**
     * Serialize the access token to a string
     * @param token The token to serialize
     * @return The serialized token
     */
    fun serializeAccessToken(token: AccessToken) : String

    /**
     * Deserialize the access token from a string
     * @param token the serialized token
     * @return the token
     */
    fun deserializeAccessToken(token: String) : AccessToken
}
