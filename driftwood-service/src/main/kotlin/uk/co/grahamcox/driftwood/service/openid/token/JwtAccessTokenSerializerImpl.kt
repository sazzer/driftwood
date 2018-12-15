package uk.co.grahamcox.driftwood.service.openid.token

import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.Verifier
import io.fusionauth.jwt.domain.JWT
import org.slf4j.LoggerFactory
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.openid.scopes.Scope
import uk.co.grahamcox.driftwood.service.openid.scopes.ScopeRegistry
import uk.co.grahamcox.driftwood.service.users.UserId
import java.time.ZoneId
import java.util.*

/**
 * JWT Implementation of the Access Token Serializer
 */
class JwtAccessTokenSerializerImpl(
        private val signer: Signer,
        private val verifier: Verifier,
        private val scopeRegistry: ScopeRegistry
) : AccessTokenSerializer {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(JwtAccessTokenSerializerImpl::class.java)

        /** The Zone ID for UTC */
        private val UTC_ZONE = ZoneId.of("UTC")
    }

    /**
     * Serialize the access token to a string
     * @param token The token to serialize
     * @return The serialized token
     */
    override fun serializeAccessToken(token: AccessToken): String {
        val jwt = JWT()
        jwt.uniqueId = token.id.id.toString()
        jwt.issuedAt = token.issued.atZone(UTC_ZONE)
        jwt.notBefore = token.issued.atZone(UTC_ZONE)
        jwt.expiration = token.expires.atZone(UTC_ZONE)
        jwt.subject = token.user.id.toString()
        jwt.audience = token.client.id.toString()
        jwt.issuer = JwtAccessTokenSerializerImpl::class.qualifiedName
        jwt.addClaim("scopes", token.scopes.map(Scope::id))

        val encoded = JWT.getEncoder().encode(jwt, signer)
        LOG.debug("Encoded access token {} into {}", token, encoded)
        return encoded
    }

    /**
     * Deserialize the access token from a string
     * @param token the serialized token
     * @return the token
     */
    override fun deserializeAccessToken(token: String): AccessToken {
        val decoded = JWT.getDecoder().decode(token, verifier)

        val result = AccessToken(
                id = AccessTokenId(UUID.fromString(decoded.uniqueId)),
                issued = decoded.issuedAt.toInstant(),
                expires = decoded.expiration.toInstant(),
                user = UserId(UUID.fromString(decoded.subject)),
                client = ClientId(UUID.fromString(decoded.audience.toString())),
                scopes = decoded.getList("scopes")
                        .map(Any::toString)
                        .mapNotNull(scopeRegistry::getScopeById)
                        .toSet()
        )

        LOG.debug("Decoded access token {} from {}", result, token)
        return result
    }
}
