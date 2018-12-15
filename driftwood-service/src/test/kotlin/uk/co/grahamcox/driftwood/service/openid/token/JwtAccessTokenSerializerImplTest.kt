package uk.co.grahamcox.driftwood.service.openid.token

import io.fusionauth.jwt.hmac.HMACSigner
import io.fusionauth.jwt.hmac.HMACVerifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
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
            expires = Instant.parse("2018-12-25T11:48:00Z"),
            scopes = setOf(GlobalScopes.ALL)
    )

    /** The serialized access token */
    private val serialized = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyZDdkYTEwNC1hZTQwLTQwNzgtYmU0OC00MTQ0YTBkMjdkYWEiLCJleHAiOjE1NDU3Mzg0ODAsImlhdCI6MTU0MzY2NDg4MCwiaXNzIjoidWsuY28uZ3JhaGFtY294LmRyaWZ0d29vZC5zZXJ2aWNlLm9wZW5pZC50b2tlbi5Kd3RBY2Nlc3NUb2tlblNlcmlhbGl6ZXJJbXBsIiwibmJmIjoxNTQzNjY0ODgwLCJzdWIiOiJhMGExYzAzNC1kMDhiLTQ4NzktOTEwMS1mZWRiMTZkY2FhMmQiLCJqdGkiOiIxODRlN2I0ZS0yMTkzLTQ4MjYtYjUxNC04OTM3YjRkNTQ2MjMiLCJzY29wZXMiOlsiKiJdfQ.mq44uI2QkqtIP9iaRqmR4vYPpmpGG6PwkP3_TPek3h66adbIpahNV2Sui2dw_Zs5-du4_50_40bP6s0b7ARphQ"

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
}
