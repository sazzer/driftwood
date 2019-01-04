package uk.co.grahamcox.driftwood.service.authentication

import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Unit tests for the Authenticator Registry
 */
internal class AuthenticatorRegistryTest {
    /** An authenticator */
    private val google = mockk<Authenticator>()

    /** An authenticator */
    private val twitter = mockk<Authenticator>()

    /** An authenticator */
    private val facebook = mockk<Authenticator>()

    /** The test subject */
    private val testSubject = AuthenticatorRegistry(mapOf(
            "google" to google,
            "twitter" to twitter,
            "facebook" to facebook
    ))

    /**
     * Test getting the names of the authenticators
     */
    @Test
    fun testNames() {
        Assertions.assertEquals(listOf("facebook", "google", "twitter"), testSubject.names)
    }

    /**
     * Test getting an authenticator
     */
    @Test
    fun testGet() {
        Assertions.assertSame(google, testSubject.get("google"))
    }

    /**
     * Test getting an authenticator
     */
    @Test
    fun testGetOperator() {
        Assertions.assertSame(twitter, testSubject["twitter"])
    }

    /**
     * Test getting an authenticator that doesn't exist
     */
    @Test
    fun testGetUnknown() {
        val e = Assertions.assertThrows(UnknownAuthenticatorException::class.java) {
            testSubject["unknown"]
        }
        Assertions.assertEquals("unknown", e.name)
    }
}
