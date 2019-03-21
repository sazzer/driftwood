package uk.co.grahamcox.skl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

/**
 * Unit tests for the SQL Builder DSL
 */
internal class DSLTest {
    /**
     * Test cases for building SELECT statements
     */
    @TestFactory
    fun testBuildSelects(): List<DynamicTest> {
        data class TestCase(
                val name: String,
                val test: () -> SelectBuilder,
                val expectedSql: String,
                val expectedBinds: Map<String, Any?>
        )

        val tests = listOf(
                TestCase(
                        name = "Trivial select from one table",
                        test = {
                            select {
                                from("theTable")
                            }
                        },
                        expectedSql = "SELECT * FROM theTable",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Trivial select from two tables using varargs",
                        test = {
                            select {
                                from("theTable", "otherTable")
                            }
                        },
                        expectedSql = "SELECT * FROM theTable, otherTable",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Trivial select from two tables separate calls",
                        test = {
                            select {
                                from("theTable")
                                from("otherTable")
                            }
                        },
                        expectedSql = "SELECT * FROM theTable, otherTable",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a single trivial Where clause containing numbers",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    eq(1, 1)
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (1 = 1)",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a single trivial Where clause containing string",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    ne("foo", "bar")
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE ('foo' != 'bar')",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a single null check",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    isNull("foo")
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE ('foo' IS NULL)",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a single not null check",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    notNull(1)
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (1 IS NOT NULL)",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with two clauses",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    notNull(1)
                                    eq("foo", "bar")
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (1 IS NOT NULL AND 'foo' = 'bar')",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a nested AND clause",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    notNull(1)
                                    and {
                                        eq("foo", "bar")
                                        ne(1, 2)
                                    }
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (1 IS NOT NULL AND ('foo' = 'bar' AND 1 != 2))",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a nested OR clause",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    notNull(1)
                                    or {
                                        eq("foo", "bar")
                                        ne(1, 2)
                                    }
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (1 IS NOT NULL AND ('foo' = 'bar' OR 1 != 2))",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a doubly nested OR clause",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    notNull(1)
                                    or {
                                        eq("foo", "bar")
                                        or {
                                            ne(1, 2)
                                            ne(1, 3)
                                        }
                                    }
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (1 IS NOT NULL AND ('foo' = 'bar' OR (1 != 2 OR 1 != 3)))",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a bind term",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    eq("foo", bind("bar"))
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE ('foo' = :bv0)",
                        expectedBinds = mapOf("bv0" to "bar")
                ),
                TestCase(
                        name = "Select with two bind terms",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    eq("foo", bind("bar"))
                                    ne("foo", bind(1))
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE ('foo' = :bv0 AND 'foo' != :bv1)",
                        expectedBinds = mapOf("bv0" to "bar", "bv1" to 1)
                ),
                TestCase(
                        name = "Select with a field term",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    eq("foo", field("bar"))
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE ('foo' = bar)",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a field term with table name",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    eq("foo", field("theTable", "bar"))
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE ('foo' = theTable.bar)",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with a field term and bind term",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    eq(field("bar"), bind(5))
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (bar = :bv0)",
                        expectedBinds = mapOf("bv0" to 5)
                ),
                TestCase(
                        name = "Select with a field term and bind term that is case to a different type",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    eq(field("bar"), cast(bind(5), "jsonb"))
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (bar = :bv0::jsonb)",
                        expectedBinds = mapOf("bv0" to 5)
                ),
                TestCase(
                        name = "Select with return field",
                        test = {
                            select {
                                from("theTable")
                                returning(field("bar"))
                            }
                        },
                        expectedSql = "SELECT bar FROM theTable",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select with multiple return fields",
                        test = {
                            select {
                                from("theTable")
                                returning(field("foo"))
                                returning(field("bar"))
                            }
                        },
                        expectedSql = "SELECT foo, bar FROM theTable",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select returning an aliased value",
                        test = {
                            select {
                                from("theTable")
                                returning(1, "one")
                            }
                        },
                        expectedSql = "SELECT 1 AS one FROM theTable",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select returning a field with a condition",
                        test = {
                            select {
                                from("theTable")
                                returning(field("foo"))
                                where {
                                    eq(field("bar"), bind("baz"))
                                }
                            }
                        },
                        expectedSql = "SELECT foo FROM theTable WHERE (bar = :bv0)",
                        expectedBinds = mapOf("bv0" to "baz")
                )
        )

        return tests.map { test ->
            DynamicTest.dynamicTest(test.name) {
                val query = test.test().build()

                Assertions.assertEquals(test.expectedSql, query.sql)
                Assertions.assertEquals(test.expectedBinds, query.binds)
            }
        }
    }
}
