package uk.co.grahamcox.driftwood.service.openid.scopes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

/**
 * Unit tests for the Scope Registry
 */
internal class ScopeRegistryTest {
    /**
     * Test building the Scope Registry for no Scopes
     */
    @Test
    fun testBuildNoScopes() {
        val testSubject = ScopeRegistry(emptySet())

        Assertions.assertTrue(testSubject.scopeValues.isEmpty())
    }

    /**
     * Test building the Scope Registry for the Global Scopes
     */
    @Test
    fun testBuildGlobalScopes() {
        val testSubject = ScopeRegistry(setOf(GlobalScopes::class.java))

        Assertions.assertEquals(setOf(GlobalScopes.ALL), testSubject.scopeValues)
    }

    /**
     * Test building the Scope Registry for an invalid type
     */
    @Test
    fun testBuildInvalidType() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ScopeRegistry(setOf(String::class.java))
        }
    }

    /**
     * Test building the Scope Registry for duplicate Scope IDs
     */
    @Test
    fun testDuplicateIds() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ScopeRegistry(setOf(GlobalScopes::class.java, OtherScopes::class.java))
        }
    }

    /**
     * Test getting scopes by ID
     */
    @Test
    fun testGetScopeById() {
        val testSubject = ScopeRegistry(setOf(GlobalScopes::class.java))

        Assertions.assertEquals(GlobalScopes.ALL, testSubject.getScopeById("*"))
    }

    /**
     * Test parsing some scope strings
     */
    @TestFactory
    fun testParseScopesSuccess(): List<DynamicTest> {
        val testSubject = ScopeRegistry(setOf(OtherScopes::class.java))

        val data = mapOf(
                "" to emptySet<Scope>(),
                "*" to setOf(OtherScopes.OTHER),
                "test" to setOf(OtherScopes.TEST),
                "a:b:c" to setOf(OtherScopes.THIRD),
                "* test" to setOf(OtherScopes.OTHER, OtherScopes.TEST),
                "* test a:b:c" to setOf(OtherScopes.OTHER, OtherScopes.TEST, OtherScopes.THIRD),
                "  *  " to setOf(OtherScopes.OTHER),
                "* * * * *" to setOf(OtherScopes.OTHER)
        )
        return data
                .map { (input, expected) ->
                    DynamicTest.dynamicTest("$input -> $expected") {
                        val result = testSubject.parseScopes(input)
                        Assertions.assertEquals(expected, result)
                    }
        }
    }

    @TestFactory
    fun testParseUnknownScopes(): List<DynamicTest> {
        val testSubject = ScopeRegistry(setOf(OtherScopes::class.java))

        val data = mapOf(
                "unknown" to setOf("unknown"),
                "* unknown" to setOf("unknown"),
                "a b c" to setOf("a", "b", "c")
        )
        return data
                .map { (input, expected) ->
                    DynamicTest.dynamicTest("$input -> $expected") {
                        val result = Assertions.assertThrows(UnknownScopesException::class.java) {
                            testSubject.parseScopes(input)
                        }
                        Assertions.assertEquals(expected, result.scopes)
                    }
                }
    }

    /**
     * Another Scope enum that contains duplicate IDs to the Global Scopes
     */
    enum class OtherScopes(override val id: String) : Scope {
        OTHER("*"),
        TEST("test"),
        THIRD("a:b:c")
    }
}
