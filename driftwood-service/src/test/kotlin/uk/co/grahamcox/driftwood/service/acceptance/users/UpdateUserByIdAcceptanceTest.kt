package uk.co.grahamcox.driftwood.service.acceptance.users

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import uk.co.grahamcox.driftwood.service.acceptance.UserAcceptanceTestBase
import java.util.*

/**
 * Acceptance Tests for updating users
 */
class UpdateUserByIdAcceptanceTest : UserAcceptanceTestBase() {
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
     * Test updating the user that we are logged in as
     */
    @Test
    fun updateMyUser() {
        useTokenForUser(userId)

        val userResponse = requester.put("/api/users/$userId",
                mapOf(
                        "name" to "Test User",
                        "email" to "test@example.com",
                        "logins" to listOf(
                                mapOf(
                                        "provider" to "google",
                                        "providerId" to "654321",
                                        "displayName" to "test@example.com"
                                )
                        )
                ))

        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, userResponse.statusCode) },

                Executable { userResponse.assertBody("""{
                    "id": "$userId",
                    "name": "Test User",
                    "email": "test@example.com",
                    "logins": [
                        {
                            "provider": "google",
                            "providerId": "654321",
                            "displayName": "test@example.com"
                        }
                    ]
                }""".trimMargin()) }
        )
    }
}
