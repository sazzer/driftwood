package uk.co.grahamcox.driftwood.service.authentication.google

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

/**
 * Unit tests for the Google User Loader
 */
internal class GoogleUserLoaderTest {
    /** The mock Access Token Loader */
    private val accessTokenLoader = mockk<GoogleAccessTokenLoader>()

    /** The test subject */
    private val testSubject = GoogleUserLoader(accessTokenLoader)

    /**
     * Test successfully loading a user
     */
    @Test
    fun testLoadUser() {
        every { accessTokenLoader.load("googleAuthCode") } returns GoogleAccessToken(
                accessToken = "googleAccessToken",
                tokenType = "Bearer",
                expiresIn = 3600,
                // This is known from jwt.io to represent name="Test User", email="test@example.com", sub="1234321"
                idToken = "eyJhbGciOiJIUzI1NiIsImtpZCI6Ijc5NzhhOTEzNDcyNjFhMjkxYmQ3MWRjYWI0YTQ2NGJlN2QyNzk2NjYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0ODQ2MjUzNTQ4NDUtZDh2M3BrbjVjb3JuZWVyaGJoNGMwM2RnNDJudmozNjAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0ODQ2MjUzNTQ4NDUtZDh2M3BrbjVjb3JuZWVyaGJoNGMwM2RnNDJudmozNjAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMjM0MzIxIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJUZXN0IFVzZXIiLCJnaXZlbl9uYW1lIjoiVGVzdCIsImZhbWlseV9uYW1lIjoiVXNlciIsImxvY2FsZSI6ImVuLUdCIiwiaWF0IjoxNTQ2MzU2NTQyLCJleHAiOjM1NDYzNjAxNDJ9.S8x03i2Q8fhzjfmb2lXk-SgcgCwZ4wsYKgQd-6jSp0c"
        )

        val user = testSubject.loadExternalUser(mapOf("code" to "googleAuthCode"), "googleAuthCode")

        Assertions.assertAll(
                Executable { Assertions.assertEquals("Test User", user.name) },
                Executable { Assertions.assertEquals("test@example.com", user.email) },
                Executable { Assertions.assertEquals("1234321", user.id) },
                Executable { Assertions.assertEquals("test@example.com", user.displayName) }
        )
    }
}
