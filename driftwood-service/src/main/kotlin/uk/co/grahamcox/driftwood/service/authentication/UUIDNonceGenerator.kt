package uk.co.grahamcox.driftwood.service.authentication

import java.util.*

/**
 * Mechanism to generate a UUID as a Nonce
 */
class UUIDNonceGenerator : () -> String {
    /**
     * Generate the Nonce
     * @return the nonce
     */
    override operator fun invoke() = UUID.randomUUID().toString()
}
