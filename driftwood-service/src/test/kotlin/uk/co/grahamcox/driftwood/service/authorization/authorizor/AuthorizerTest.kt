package uk.co.grahamcox.driftwood.service.authorization.authorizor

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.openid.token.AccessToken
import uk.co.grahamcox.driftwood.service.openid.token.AccessTokenId
import uk.co.grahamcox.driftwood.service.users.UserId
import java.time.Instant
import java.util.*

/**
 * Unit tests for the Authorizer
 */
class AuthorizerTest {
    /** The User ID */
    private val userId = UserId(UUID.randomUUID())

    /** The Client ID */
    private val clientId = ClientId(UUID.randomUUID())

    /** An access token to use */
    private val accessToken = AccessToken(
            id = AccessTokenId(UUID.randomUUID()),
            user = userId,
            client = clientId,
            issued = Instant.EPOCH,
            expires = Instant.MAX,
            scopes = emptySet()
    )


    /**
     * Test authorization with no checks
     */
    @Test
    fun testNoChecks() {
        val testSubject = AuthorizerImpl(accessToken)
        val result = testSubject {}

        Assertions.assertTrue(result is AuthorizationSuccessResult)
    }

    /**
     * Test authorization when we are logged in as the desired user
     */
    @Test
    fun testSameUser() {
        val testSubject = AuthorizerImpl(accessToken)
        val result = testSubject {
            sameUser(userId)
        }

        Assertions.assertTrue(result is AuthorizationSuccessResult)
    }

    /**
     * Test authorization when we are logged in as the wrong user
     */
    @Test
    fun testOtherUser() {
        val testSubject = AuthorizerImpl(accessToken)
        val result = testSubject {
            sameUser(UserId(UUID.randomUUID()))
        }

        Assertions.assertTrue(result is AuthorizationFailedResult)
    }

    /**
     * Test authorization when we are not logged in
     */
    @Test
    fun testNoUser() {
        val testSubject = AuthorizerImpl(null)
        val result = testSubject {
            sameUser(UserId(UUID.randomUUID()))
        }

        Assertions.assertTrue(result is AuthorizationFailedResult)
    }
}
