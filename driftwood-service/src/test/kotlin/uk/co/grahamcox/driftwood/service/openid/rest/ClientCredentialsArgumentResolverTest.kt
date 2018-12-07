package uk.co.grahamcox.driftwood.service.openid.rest

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest
import uk.co.grahamcox.driftwood.service.client.ClientId
import uk.co.grahamcox.driftwood.service.client.ClientSecret
import java.util.*

/**
 * Unit tests for the Client Credentials Argument Resolver
 */
internal class ClientCredentialsArgumentResolverTest {
    /** The test subject */
    private val testSubject = ClientCredentialsArgumentResolver()

    /**
     * Test which fields the test subject can support
     */
    @TestFactory
    fun testSupports(): List<DynamicTest> {
        return listOf(
                ClientCredentials::class.java to true,
                String::class.java to false,
                ClientId::class.java to false,
                ClientSecret::class.java to false
        ).map {(type, expected) ->
            DynamicTest.dynamicTest(type.toString()) {
                val parameter = mockk<MethodParameter> {
                    every { parameterType } returns type
                }
                Assertions.assertEquals(expected, testSubject.supportsParameter(parameter))
            }
        }
    }

    /**
     * Test parsing the Authorization header to get the credentials
     */
    @TestFactory
    fun testParseHeader(): List<DynamicTest> {
        return listOf(
                null to null,
                "Bearer abc" to null,
                "Basic NDRFODFGMEItNzM5OS00OTYxLThDRTAtQTYxNEU2Q0JFRjAzOjI2NEZDNzJBLUQ3MDEtNDhENS1CRDlELUI0QzA5MkRCMDA3OA=="
                        to ClientCredentials(ClientId(UUID.fromString("44E81F0B-7399-4961-8CE0-A614E6CBEF03")), ClientSecret(UUID.fromString("264FC72A-D701-48D5-BD9D-B4C092DB0078"))),
                "Basic YTpi" to null,
                "Basic NDRFODFGMEItNzM5OS00OTYxLThDRTAtQTYxNEU2Q0JFRjAzOg==" to null,
                "Basic NDRFODFGMEItNzM5OS00OTYxLThDRTAtQTYxNEU2Q0JFRjAz" to null
        ).map { (header, credentials) ->
            DynamicTest.dynamicTest("$header -> $credentials") {
                val request = mockk<NativeWebRequest>() {
                    every { getHeader("Authorization") } returns header
                }

                val result = testSubject.resolveArgument(
                        mockk(),
                        null,
                        request,
                        null
                )

                Assertions.assertEquals(credentials, result)
            }
        }
    }
}
