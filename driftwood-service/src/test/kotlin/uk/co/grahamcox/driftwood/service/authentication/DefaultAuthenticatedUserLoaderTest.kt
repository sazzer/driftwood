package uk.co.grahamcox.driftwood.service.authentication

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.co.grahamcox.driftwood.service.model.Identity
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.users.UserData
import uk.co.grahamcox.driftwood.service.users.UserId
import uk.co.grahamcox.driftwood.service.users.UserLoginData
import uk.co.grahamcox.driftwood.service.users.UserService
import java.time.Instant
import java.util.*

/**
 * Unit tests for the Default Authenticated User Loader
 */
internal class DefaultAuthenticatedUserLoaderTest {
    /** The mock External User Loader */
    private val externalUserLoader: ExternalUserLoader = mockk()

    /** The mock User Service */
    private val userService: UserService = mockk()

    /** The test subject */
    private val testSubject = DefaultAuthenticatedUserLoader("google", externalUserLoader, userService)

    /** The external user */
    private val externalUser = ExternalUser(
            id = "externalId",
            name = "Test User",
            email = "test@example.com",
            displayName = "test@example.com"
    )

    /** The internal version of the user */
    private val internalUser = Resource(
            identity = Identity(
                    id = UserId(UUID.randomUUID()),
                    version = UUID.randomUUID(),
                    created = Instant.now(),
                    updated = Instant.now()
            ),
            data = UserData(
                    name = "Test User",
                    email = "test@example.com",
                    logins = setOf(
                            UserLoginData(
                                    provider = "google",
                                    providerId = "externalId",
                                    displayName = "test@example.com"
                            )
                    )
            )
    )
    /**
     * Expect that an External User is retrieved
     */
    @BeforeEach
    fun expectExternalUser() {
        every { externalUserLoader.loadExternalUser(mapOf("code" to "code"), "code") } returns externalUser
    }

    /**
     * Test when the user being authenticated is unknown
     */
    @Test
    fun testUnknownUser() {
        every { userService.getByProviderId("google", "externalId") } returns null
        every { userService.save(internalUser.data) } returns internalUser

        val user = testSubject.authenticateUser(mapOf("code" to "code"), "code")

        Assertions.assertEquals(internalUser, user)
    }

    /**
     * Test when the user being authenticated is known
     */
    @Test
    fun testKnownUser() {
        every { userService.getByProviderId("google", "externalId") } returns internalUser

        val user = testSubject.authenticateUser(mapOf("code" to "code"), "code")

        Assertions.assertEquals(internalUser, user)
    }
}
