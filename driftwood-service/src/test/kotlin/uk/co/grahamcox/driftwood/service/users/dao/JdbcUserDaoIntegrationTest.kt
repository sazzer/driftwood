package uk.co.grahamcox.driftwood.service.users.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import uk.co.grahamcox.driftwood.service.dao.DaoTestBase
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import uk.co.grahamcox.driftwood.service.model.Identity
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.users.UserData
import uk.co.grahamcox.driftwood.service.users.UserId
import uk.co.grahamcox.driftwood.service.users.UserLoginData
import uk.co.grahamcox.driftwood.service.users.UserNotFoundException
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * Integration test for the JDBC User DAO
 */
internal class JdbcUserDaoIntegrationTest : DaoTestBase() {
    companion object {
        /** The ID of the user to use */
        private val USER_ID = UserId(UUID.randomUUID())
    }

    /** The means to seed user records */
    @Autowired
    private lateinit var userSeeder: DatabaseSeeder

    /** The test subject */
    @Autowired
    private lateinit var testSubject: JdbcUserDao

    /** The "current time" */
    @Autowired
    @Qualifier("currentTime")
    private lateinit var currentTime: Instant

    /**
     * Gest getting a user by ID when the user doesn't exist
     */
    @Test
    fun getUnknownUserById() {
        val e = Assertions.assertThrows(UserNotFoundException::class.java) {
            testSubject.getById(USER_ID)
        }

        Assertions.assertEquals(USER_ID, e.id)
    }

    /**
     * Test getting a user by ID when the user does exist and has the most simple of data
     */
    @Test
    fun getSimpleUserById() {
        val version = UUID.randomUUID()

        userSeeder(
                "userId" to USER_ID.id.toString(),
                "version" to version.toString(),
                "created" to currentTime.toString(),
                "updated" to currentTime.toString(),
                "name" to "Graham"
        )

        val user = testSubject.getById(USER_ID)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(USER_ID, user.identity.id) },
                Executable { Assertions.assertEquals(version, user.identity.version) },
                Executable { Assertions.assertEquals(currentTime, user.identity.created) },
                Executable { Assertions.assertEquals(currentTime, user.identity.updated) },

                Executable { Assertions.assertEquals("Graham", user.data.name) },
                Executable { Assertions.assertNull(user.data.email) },
                Executable { Assertions.assertEquals(emptySet<UserLoginData>(), user.data.logins) }
        )
    }

    /**
     * Test getting a user by ID when the user does exist and has fully populated data
     */
    @Test
    fun getFullUserById() {
        val version = UUID.randomUUID()

        userSeeder(
                "userId" to USER_ID.id.toString(),
                "version" to version.toString(),
                "created" to currentTime.toString(),
                "updated" to currentTime.toString(),
                "name" to "Graham",
                "email" to "graham@example.com",
                "logins" to "twitter,@graham,@graham;google,123456,graham@example.com"
        )

        val user = testSubject.getById(USER_ID)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(USER_ID, user.identity.id) },
                Executable { Assertions.assertEquals(version, user.identity.version) },
                Executable { Assertions.assertEquals(currentTime, user.identity.created) },
                Executable { Assertions.assertEquals(currentTime, user.identity.updated) },

                Executable { Assertions.assertEquals("Graham", user.data.name) },
                Executable { Assertions.assertEquals("graham@example.com", user.data.email) },
                Executable { Assertions.assertEquals(setOf(
                        UserLoginData(provider = "twitter", providerId = "@graham", displayName = "@graham"),
                        UserLoginData(provider = "google", providerId = "123456", displayName = "graham@example.com")
                ), user.data.logins) }
        )
    }

    /**
     * Test updating a user successfully
     */
    @Test
    fun updateUser() {
        val version = UUID.randomUUID()
        val createdAt = currentTime.minus(Duration.ofDays(5))

        userSeeder(
                "userId" to USER_ID.id.toString(),
                "version" to version.toString(),
                "created" to createdAt.toString(),
                "updated" to createdAt.toString(),
                "name" to "Graham"
        )

        val user = testSubject.save(Resource(
                identity = Identity(
                        id = USER_ID,
                        version = version,
                        created = createdAt,
                        updated = createdAt
                ),
                data = UserData(
                        name = "Test User",
                        email = "test@example.com",
                        logins = setOf(
                                UserLoginData(
                                        provider = "google",
                                        providerId = "654321",
                                        displayName = "test@example.com"
                                )
                        )
                )
        ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(USER_ID, user.identity.id) },
                Executable { Assertions.assertNotEquals(version, user.identity.version) },
                Executable { Assertions.assertEquals(createdAt, user.identity.created) },
                Executable { Assertions.assertEquals(currentTime, user.identity.updated) },

                Executable { Assertions.assertEquals("Test User", user.data.name) },
                Executable { Assertions.assertEquals("test@example.com", user.data.email) },
                Executable { Assertions.assertEquals(setOf(
                        UserLoginData(provider = "google", providerId = "654321", displayName = "test@example.com")
                ), user.data.logins) }
        )

        val loaded = testSubject.getById(USER_ID)

        Assertions.assertEquals(user, loaded)
    }
}
