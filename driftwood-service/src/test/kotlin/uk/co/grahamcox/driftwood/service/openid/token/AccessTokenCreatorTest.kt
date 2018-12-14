package uk.co.grahamcox.driftwood.service.openid.token

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import uk.co.grahamcox.driftwood.service.clients.ClientData
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.clients.ClientSecret
import uk.co.grahamcox.driftwood.service.model.Identity
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.openid.scopes.GlobalScopes
import uk.co.grahamcox.driftwood.service.users.UserId
import java.time.Clock
import java.time.Instant
import java.time.Period
import java.time.ZoneId
import java.util.*

/**
 * Unit tests for the Access Token Creator
 */
internal class AccessTokenCreatorTest {
    /** The "current time" */
    private val NOW = Instant.parse("2018-12-14T18:14:00Z")

    /** The token duration */
    private val duration = Period.ofDays(5)

    /** The test subject to use */
    private val testSubject = AccessTokenCreator(Clock.fixed(NOW, ZoneId.of("UTC")), duration)

    /**
     * Test creating an access token
     */
    @Test
    fun createAccessToken() {
        val clientId = ClientId(UUID.randomUUID())
        val userId = UserId(UUID.randomUUID())

        val client = Resource(
                identity = Identity(
                        id = clientId,
                        version = UUID.randomUUID(),
                        created = NOW,
                        updated = NOW
                ),
                data = ClientData(
                        name = "Test",
                        secret = ClientSecret(UUID.randomUUID()),
                        grantTypes = emptySet(),
                        responseTypes = emptySet(),
                        redirectUris = emptySet(),
                        owner = UserId(UUID.randomUUID())
                )
        )

        val token = testSubject.createToken(client, userId, setOf(GlobalScopes.ALL))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(clientId, token.client) },
                Executable { Assertions.assertEquals(userId, token.user) },
                Executable { Assertions.assertEquals(NOW, token.issued) },
                Executable { Assertions.assertEquals(NOW.plus(duration), token.expires) },
                Executable { Assertions.assertEquals(setOf(GlobalScopes.ALL), token.scopes) }
        )
    }
}
