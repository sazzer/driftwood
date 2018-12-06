package uk.co.grahamcox.driftwood.service.users.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import uk.co.grahamcox.driftwood.service.DaoTestBase
import uk.co.grahamcox.driftwood.service.users.UserId
import uk.co.grahamcox.driftwood.service.users.UserLoginData
import uk.co.grahamcox.driftwood.service.users.UserNotFoundException
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

    /** The test subject */
    private val testSubject: JdbcUserDao
        get() = JdbcUserDao(jdbcTemplate)

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
     * Gest getting a user by ID when the user does exist and has the most simple of data
     */
    @Test
    fun getSimpleUserById() {
        val version = UUID.randomUUID()
        val now = Instant.parse("2018-12-06T16:31:00Z")

        jdbcTemplate.update("""INSERT INTO users(user_id, version, created, updated, name, email, authentication)
            VALUES (:id, :version, :now, :now, :name, :email, :authentication::jsonb)""", mapOf(
                "id" to USER_ID.id,
                "version" to version,
                "now" to Date.from(now),
                "name" to "Graham",
                "email" to null,
                "authentication" to "[]"
        ))

        val user = testSubject.getById(USER_ID)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(USER_ID, user.identity.id) },
                Executable { Assertions.assertEquals(version, user.identity.version) },
                Executable { Assertions.assertEquals(now, user.identity.created) },
                Executable { Assertions.assertEquals(now, user.identity.updated) },

                Executable { Assertions.assertEquals("Graham", user.data.name) },
                Executable { Assertions.assertNull(user.data.email) },
                Executable { Assertions.assertEquals(emptySet<UserLoginData>(), user.data.logins) }
        )
    }
}
