package uk.co.grahamcox.driftwood.service.authentication.google

import io.fusionauth.jwt.Verifier
import io.fusionauth.jwt.domain.Algorithm

/**
 * JWT Verifier that doesn't actually do anything
 */
object NoopJwtVerifier : Verifier {
    /**
     * "verify" the JWT
     */
    override fun verify(algorithm: Algorithm, payload: ByteArray, signature: ByteArray) {
        // Nothing here
    }

    override fun canVerify(algorithm: Algorithm) = true
}
