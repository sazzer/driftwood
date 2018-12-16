package uk.co.grahamcox.driftwood.service.openid.token

import io.fusionauth.jwt.hmac.HMACSigner
import io.fusionauth.jwt.hmac.HMACVerifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.openid.scopes.GlobalScopes
import uk.co.grahamcox.driftwood.service.openid.scopes.ScopeRegistry
import uk.co.grahamcox.driftwood.service.users.UserId
import java.time.Instant
import java.util.*

/**
 * Unit tests for the JWT Access Token Serializer
 */
internal class JwtAccessTokenSerializerImplTest {
    /** The secret key to use */
    private val key = "MySecretKey"

    /** The test subject */
    private val testSubject = JwtAccessTokenSerializerImpl(
            signer = HMACSigner.newSHA512Signer(key),
            verifier = HMACVerifier.newVerifier(key),
            scopeRegistry = ScopeRegistry(setOf(GlobalScopes::class.java))
    )

    /** The access token to work with */
    private val token = AccessToken(
            id = AccessTokenId(UUID.fromString("184E7B4E-2193-4826-B514-8937B4D54623")),
            user = UserId(UUID.fromString("A0A1C034-D08B-4879-9101-FEDB16DCAA2D")),
            client = ClientId(UUID.fromString("2D7DA104-AE40-4078-BE48-4144A0D27DAA")),
            issued = Instant.parse("2018-12-01T11:48:00Z"),
            expires = Instant.parse("2028-12-25T11:48:00Z"),
            scopes = setOf(GlobalScopes.ALL)
    )

    /** The serialized access token */
    private val serialized = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJqdGkiOiIxODRlN2I0ZS0yMTkzLTQ4MjYtYjUxNC04OTM3YjRkNTQ2MjMiLCJzY29wZXMiOlsiKiJdfQ.0IXoGRpqUUkl8FwifoccQ4H4XOjT8LXW0hArXsiNNOdIZ3YdKmxgthlZ8JWsO8gRI1qb68l8yT5XrN2X3Y8-PA"

    /**
     * Test serializing an access token
     */
    @Test
    fun testSerialize() {
        val result = testSubject.serializeAccessToken(token)
        Assertions.assertEquals(serialized, result)
    }

    /**
     * Test deserializing an access token
     */
    @Test
    fun testDeserialize() {
        val result = testSubject.deserializeAccessToken(serialized)
        Assertions.assertEquals(token, result)
    }

    /**
     * Test deserializing an invalid access token
     */
    @TestFactory
    fun testDeserializeInvalid(): List<DynamicTest> {
        val data = listOf(
                "Invalid structure" to serialized.substring(0, serialized.lastIndexOf(".")),
                "Invalid signature" to serialized.dropLast(1) + "0",
                // These were all generated on jwt.io
                "No Audience" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJqdGkiOiIxODRlN2I0ZS0yMTkzLTQ4MjYtYjUxNC04OTM3YjRkNTQ2MjMiLCJzY29wZXMiOlsiKiJdfQ.Hhu8gtTbt76QY0JcUVeWqk4fhRQpGywQ1uUSxtoDORg3ZfDRCzui29iSgo34gtxeucqa7n0Sxu40HkeV4vIjmw",
                "No Expiration Date" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJpYXQiOjE1NDM2NjQ4ODAsImlzcyI6InVrLmNvLmdyYWhhbWNveC5kcmlmdHdvb2Quc2VydmljZS5vcGVuaWQudG9rZW4uSnd0QWNjZXNzVG9rZW5TZXJpYWxpemVySW1wbCIsIm5iZiI6MTU0MzY2NDg4MCwic3ViIjoiYTBhMWMwMzQtZDA4Yi00ODc5LTkxMDEtZmVkYjE2ZGNhYTJkIiwianRpIjoiMTg0ZTdiNGUtMjE5My00ODI2LWI1MTQtODkzN2I0ZDU0NjIzIiwic2NvcGVzIjpbIioiXX0.z9fJkLql-Wya45LDwlDImmlX8faKq6Xqr6IYf035nDmKAXk5ZS87TKJyCjEVVe8mqCiwJ1Tzd38oqft_CTqQeQ",
                "No Issued At Date" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlzcyI6InVrLmNvLmdyYWhhbWNveC5kcmlmdHdvb2Quc2VydmljZS5vcGVuaWQudG9rZW4uSnd0QWNjZXNzVG9rZW5TZXJpYWxpemVySW1wbCIsIm5iZiI6MTU0MzY2NDg4MCwic3ViIjoiYTBhMWMwMzQtZDA4Yi00ODc5LTkxMDEtZmVkYjE2ZGNhYTJkIiwianRpIjoiMTg0ZTdiNGUtMjE5My00ODI2LWI1MTQtODkzN2I0ZDU0NjIzIiwic2NvcGVzIjpbIioiXX0.-Vu9lJZVFYSUiK6GqwFIfUTFwj0xr1WFmFw_1qtXSHpzZdakdW8qVeEZHuARV5vBzLuZWj2Oad-s9zJA5__b9g",
                "No Issuer" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJqdGkiOiIxODRlN2I0ZS0yMTkzLTQ4MjYtYjUxNC04OTM3YjRkNTQ2MjMiLCJzY29wZXMiOlsiKiJdfQ.d93iCABEDBYd6XSCLgvgCViTv7SpsctBinnfgNp7NgzMxXNABiZCAaxTRl9VuWEqRTdoFlsza3tZIm61yY_asw",
                "No Not Before Date" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwic3ViIjoiYTBhMWMwMzQtZDA4Yi00ODc5LTkxMDEtZmVkYjE2ZGNhYTJkIiwianRpIjoiMTg0ZTdiNGUtMjE5My00ODI2LWI1MTQtODkzN2I0ZDU0NjIzIiwic2NvcGVzIjpbIioiXX0.SOJTY4YtuXjxBnBgxFT5lj-ZLGd9e-Fj1cu_NHfPBgsbKUJLRyYNugoSx7OxySmUOciGWSiz4hYJXRbm01To8w",
                "No Subject" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJqdGkiOiIxODRlN2I0ZS0yMTkzLTQ4MjYtYjUxNC04OTM3YjRkNTQ2MjMiLCJzY29wZXMiOlsiKiJdfQ.__avDN5xV3jSFNXPos9AnNulx2x7iMTK0ONTlacnxh7-q-WQnv5M3xaZD1AKkmmPH4LtHGdH8cP5BIRBIUxBzg",
                "No Unique ID" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJzY29wZXMiOlsiKiJdfQ.qZdVMb0SZONDtsyLh7zDGuVKZMdqx4ppockq17YqnUkZpRbYfdJYzjVzL3NsWbPQKet9v4S-1_QXxsLwXbDcLQ",
                "No Scopes" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJqdGkiOiIxODRlN2I0ZS0yMTkzLTQ4MjYtYjUxNC04OTM3YjRkNTQ2MjMifQ.mskznRCZ8OMpAvXAdsCmFlj7vo7QW6EP_SOJWYuIXUAKaJW2DTI5yEtljKeOsLBu2IR0XgfLpScPwJLE5H7I_w",

                "Wrong Issuer" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoid3JvbmciLCJuYmYiOjE1NDM2NjQ4ODAsInN1YiI6ImEwYTFjMDM0LWQwOGItNDg3OS05MTAxLWZlZGIxNmRjYWEyZCIsImp0aSI6IjE4NGU3YjRlLTIxOTMtNDgyNi1iNTE0LTg5MzdiNGQ1NDYyMyIsInNjb3BlcyI6WyIqIl19.INlH890TyeOeiTLPzE0mibjfM-0Ol8TMF29wCBOAUnuhekjbCf_6sik8I1l42DHtKiDblxGPVm5uukNMBosrGA",

                "Invalid Audience Structure" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJ3cm9uZyIsImV4cCI6MTg2MTM1NzY4MCwiaWF0IjoxNTQzNjY0ODgwLCJpc3MiOiJ1ay5jby5ncmFoYW1jb3guZHJpZnR3b29kLnNlcnZpY2Uub3BlbmlkLnRva2VuLkp3dEFjY2Vzc1Rva2VuU2VyaWFsaXplckltcGwiLCJuYmYiOjE1NDM2NjQ4ODAsInN1YiI6ImEwYTFjMDM0LWQwOGItNDg3OS05MTAxLWZlZGIxNmRjYWEyZCIsImp0aSI6IjE4NGU3YjRlLTIxOTMtNDgyNi1iNTE0LTg5MzdiNGQ1NDYyMyIsInNjb3BlcyI6WyIqIl19.Y10848QQXKZJrI4idk3wCmFEICtxuq2LYQ2ZY0fKDU9J4SXXNo876Kpumv7IYKqb9RdJSevn6fvu7VZ55WpsEQ",
                "Invalid Subject Structure" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJ3cm9uZyIsImp0aSI6IjE4NGU3YjRlLTIxOTMtNDgyNi1iNTE0LTg5MzdiNGQ1NDYyMyIsInNjb3BlcyI6WyIqIl19.0HlAzNUr6hhuv0w05OJuWNEGtWZ17I26JKJr6DQNWiAwLevOMDLd7VygDeZRKAgRPkLcj9kIqV1D7q8Y7pj1HQ",
                "Invalid Unique ID Structure" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJqdGkiOiJ3cm9uZyIsInNjb3BlcyI6WyIqIl19.WxpAPl-YKPKDPklUGjyhdG0wkXLMPvu49NpDIGz7yRygDp1E7f8iI5tUQkDBxkHHZNHRulDOEdOw8ypsb8GLgg",
                "Invalid Scopes Structure" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE4NjEzNTc2ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJqdGkiOiIxODRlN2I0ZS0yMTkzLTQ4MjYtYjUxNC04OTM3YjRkNTQ2MjMiLCJzY29wZXMiOiIqIn0.0aMclTKIU3QjIhk4tlAca-sGHnUyteyiqIuHeEJMtEEpXhy0U4d6hu9KrayyPrQxbLzrHSu57TFFWIcums6Pog",

                "Expiry in the past" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE1NDU3Mzg0OCwiaWF0IjoxNTQzNjY0ODgwLCJpc3MiOiJ1ay5jby5ncmFoYW1jb3guZHJpZnR3b29kLnNlcnZpY2Uub3BlbmlkLnRva2VuLkp3dEFjY2Vzc1Rva2VuU2VyaWFsaXplckltcGwiLCJuYmYiOjE1NDM2NjQ4ODAsInN1YiI6ImEwYTFjMDM0LWQwOGItNDg3OS05MTAxLWZlZGIxNmRjYWEyZCIsImp0aSI6IjE4NGU3YjRlLTIxOTMtNDgyNi1iNTE0LTg5MzdiNGQ1NDYyMyIsInNjb3BlcyI6WyIqIl19.3AZqebm3e1qnZriJ3vImHBHJhrZWm4vo-6njJASeXDnAedRDsx-1vSNiFyvMPAog8NyM_51zW8JwCCF3GUh6fA",
                "Not Before Date in the future" to "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE1NDU3Mzg0ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoyNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJqdGkiOiIxODRlN2I0ZS0yMTkzLTQ4MjYtYjUxNC04OTM3YjRkNTQ2MjMiLCJzY29wZXMiOlsiKiJdfQ.lVJaUOhwNXnVdoq0V-Yph1nBIyPnN7xtVIN6SQIy1ymaksfeI18n9fpWEtrM7DeAOpKY5BZ1U1TRx5eUa08Cdg"
        )

        return data.map {(name, input) ->
            DynamicTest.dynamicTest(name) {
                Assertions.assertThrows(InvalidAccessTokenException::class.java) {
                    testSubject.deserializeAccessToken(input)
                }
            }
        }
    }
}
