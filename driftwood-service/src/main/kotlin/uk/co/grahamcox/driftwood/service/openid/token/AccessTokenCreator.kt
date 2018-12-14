package uk.co.grahamcox.driftwood.service.openid.token

import org.slf4j.LoggerFactory
import uk.co.grahamcox.driftwood.service.clients.ClientData
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.openid.scopes.Scope
import uk.co.grahamcox.driftwood.service.users.UserId
import java.time.Clock
import java.time.temporal.TemporalAmount
import java.util.*

/**
 * The means to create an access token for a user
 * @property clock The clock to use
 * @property duration The duration of the access tokens
 */
class AccessTokenCreator(
        private val clock: Clock,
        private val duration: TemporalAmount
) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(AccessTokenCreator::class.java)
    }

    /**
     * Create a token for the given user from the given client
     * @param client The client issuing the token
     * @param user The user being issued the token
     * @param scopes The scopes in the token
     * @return the token
     */
    fun createToken(client: Resource<ClientId, ClientData>,
                    user: UserId,
                    scopes: Set<Scope>) : AccessToken {
        val id = AccessTokenId(UUID.randomUUID())
        val now = clock.instant()
        val expires = now.plus(duration)

        val token = AccessToken(
                id = id,
                client = client.identity.id,
                user = user,
                scopes = scopes,
                issued = now,
                expires = expires
        )

        LOG.debug("Created access token: {}", token)

        return token
    }
}
