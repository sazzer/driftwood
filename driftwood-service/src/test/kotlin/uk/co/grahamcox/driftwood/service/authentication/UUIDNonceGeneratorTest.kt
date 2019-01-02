package uk.co.grahamcox.driftwood.service.authentication

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test the UUID Nonce Generator
 */
internal class UUIDNonceGeneratorTest {
    /**
     * Test that the generated nonce looks like a UUID
     */
    @Test
    fun testGenerate() {
        val generator = UUIDNonceGenerator()

        val nonce = generator()
        MatcherAssert.assertThat(nonce, Matchers.matchesPattern("^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$"))
    }

    /**
     * Test that generating a large number of nonces doesn't cause any duplicates
     */
    @Test
    fun testUnique() {
        val expected = 1000
        val generator = UUIDNonceGenerator()

        val count = (1..expected).toList()
                .map { generator() }
                .toSet()
                .size

        Assertions.assertEquals(expected, count)
    }
}
