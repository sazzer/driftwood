package uk.co.grahamcox.driftwood.service.authorization

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.driftwood.service.CurrentRequest
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.openid.token.AccessToken
import uk.co.grahamcox.driftwood.service.openid.token.AccessTokenId
import uk.co.grahamcox.driftwood.service.users.UserId
import java.time.Instant
import java.util.*

/**
 * Unit tests for the Access Token Store
 */
@CurrentRequest
internal class AccessTokenStoreTest {
    /** An access token to use */
    private val accessToken = AccessToken(
            id = AccessTokenId(UUID.randomUUID()),
            user = UserId(UUID.randomUUID()),
            client = ClientId(UUID.randomUUID()),
            issued = Instant.EPOCH,
            expires = Instant.MAX,
            scopes = emptySet()
    )

    /** The test subject */
    private val testSubject = AccessTokenStore()

    /**
     * Test getting the access token when it was never set
     */
    @Test
    fun testGetWhenNotSet() {
        Assertions.assertNull(testSubject.accessToken)
    }

    /**
     * Test setting the access token and then getting it back again
     */
    @Test
    fun testGetWhenSet() {
        testSubject.accessToken = accessToken
        Assertions.assertSame(accessToken, testSubject.accessToken)
    }

    /**
     * Test getting the access token after it's been reset
     */
    @Test
    fun testGetWhenReset() {
        testSubject.accessToken = accessToken
        testSubject.reset()
        Assertions.assertNull(testSubject.accessToken)
    }
}
