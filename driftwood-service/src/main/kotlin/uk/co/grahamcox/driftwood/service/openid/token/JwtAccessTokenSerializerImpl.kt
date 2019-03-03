package uk.co.grahamcox.driftwood.service.openid.token

import io.fusionauth.jwt.JWTException
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

        /** The issuer to use */
        private val ISSUER = JwtAccessTokenSerializerImpl::class.qualifiedName
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
        jwt.issuer = ISSUER
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
    @Suppress("ComplexMethod")
    override fun deserializeAccessToken(token: String): AccessToken {
        val decoded = try {
            JWT.getDecoder().decode(token, verifier)
        } catch (e: JWTException) {
            LOG.warn("Failed to decode JWT: {}", token, e)
            throw InvalidAccessTokenException(e.message)
        }

        val missingFields = listOf(
                "uniqueId" to decoded.uniqueId,
                "issuedAt" to decoded.issuedAt,
                "notBefore" to decoded.notBefore,
                "expiration" to decoded.expiration,
                "subject" to decoded.subject,
                "audience" to decoded.audience,
                "issuer" to decoded.issuer,
                "scopes" to decoded.getObject("scopes")
        ).filter { it.second == null }
                .map { it.first }
        if (missingFields.isNotEmpty()) {
            throw InvalidAccessTokenException("Missing field: $missingFields")
        }

        if (decoded.issuer != ISSUER) {
            LOG.warn("Wrong issuer for the token")
            throw InvalidAccessTokenException("Wrong issuer for the token")
        }

        val tokenId = try {
            UUID.fromString(decoded.uniqueId)
        } catch (e: IllegalArgumentException) {
            LOG.warn("Unique ID is in the wrong format", e)
            throw InvalidAccessTokenException("Unique ID is in the wrong format")
        }

        val subject = try {
            UUID.fromString(decoded.subject)
        } catch (e: IllegalArgumentException) {
            LOG.warn("Subject is in the wrong format", e)
            throw InvalidAccessTokenException("Subject is in the wrong format")
        }

        val audience = try {
            UUID.fromString(decoded.audience.toString())
        } catch (e: IllegalArgumentException) {
            LOG.warn("Audience is in the wrong format", e)
            throw InvalidAccessTokenException("Audience is in the wrong format")
        }

        if (decoded.getObject("scopes") !is List<*>) {
            LOG.warn("Scopes is in the wrong format")
            throw InvalidAccessTokenException("Scopes is in the wrong format")
        }

        val result = AccessToken(
                id = AccessTokenId(tokenId),
                issued = decoded.issuedAt.toInstant(),
                expires = decoded.expiration.toInstant(),
                user = UserId(subject),
                client = ClientId(audience),
                scopes = decoded.getList("scopes")
                        .map(Any::toString)
                        .mapNotNull(scopeRegistry::getScopeById)
                        .toSet()
        )

        LOG.debug("Decoded access token {} from {}", result, token)
        return result
    }
}
