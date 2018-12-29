package uk.co.grahamcox.driftwood.service.acceptance.users

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import uk.co.grahamcox.driftwood.service.acceptance.UserAcceptanceTestBase
import java.util.*

/**
 * Acceptance Tests for getting users by ID
 */
class GetUserByIdAcceptanceTest : UserAcceptanceTestBase() {
    /** The ID of the User to work with */
    private val userId = UUID.randomUUID().toString()

    /**
     * Create the user to work with
     */
    @BeforeEach
    fun createUser() {
        seedUser(
                "userId" to userId,
                "created" to "2018-12-06T16:31:00Z",
                "updated" to "2018-12-06T16:31:00Z",
                "name" to "Graham",
                "email" to "graham@example.com",
                "logins" to "twitter,@graham,@graham;google,123456,graham@example.com"
        )
    }

    /**
     * Test getting the user that we are logged in as
     */
    @Test
    fun getMyUser() {
        useTokenForUser(userId)

        val userResponse = requester.get("/api/users/$userId")
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, userResponse.statusCode) },

                Executable { userResponse.assertBody("""{
                    "id": "$userId",
                    "name": "Graham",
                    "email": "graham@example.com",
                    "logins": [
                        {
                            "provider": "google",
                            "providerId": "123456",
                            "displayName": "graham@example.com"
                        }, {
                            "provider": "twitter",
                            "providerId": "@graham",
                            "displayName": "@graham"
                        }
                    ]
                }""".trimMargin()) }
        )
    }

    /**
     * Test getting a user when we are not logged in
     */
    @Test
    fun getUserUnauthenticated() {
        val userResponse = requester.get("/api/users/$userId")
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, userResponse.statusCode) },

                Executable { userResponse.assertBody("""{
                    "id": "$userId",
                    "name": "Graham"
                }""".trimMargin()) }
        )
    }

    /**
     * Test getting a different user to the one that we are logged in as
     */
    @Test
    fun getAnotherUser() {
        val otherUserId = UUID.randomUUID().toString()
        seedUser("userId" to otherUserId)
        useTokenForUser(otherUserId)

        val userResponse = requester.get("/api/users/$userId")
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, userResponse.statusCode) },

                Executable { userResponse.assertBody("""{
                    "id": "$userId",
                    "name": "Graham"
                }""".trimMargin()) }
        )
    }
}
