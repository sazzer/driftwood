package uk.co.grahamcox.skl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

/**
 * Unit tests for the SQL Builder DSL
 */
internal class DSLTest {
    data class TestCase(
            val name: String,
            val test: () -> QueryBuilder,
            val expectedSql: String,
            val expectedBinds: Map<String, Any?>
    )

    /**
     * Test cases for building SELECT statements
     */
    @TestFactory
    fun testBuildSelects(): List<DynamicTest> {

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
                        name = "Select with an empty where clause",
                        test = {
                            select {
                                from("theTable")
                                where {
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable",
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
                        name = "Select with an empty nested AND clause",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    notNull(1)
                                    and {
                                    }
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (1 IS NOT NULL)",
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
                        name = "Select with a function on both sides",
                        test = {
                            select {
                                from("theTable")
                                where {
                                    eq(function("UPPER", field("foo")), function("UPPER", bind("bar")))
                                }
                            }
                        },
                        expectedSql = "SELECT * FROM theTable WHERE (UPPER(foo) = UPPER(:bv0))",
                        expectedBinds = mapOf("bv0" to "bar")
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
                ),
                TestCase(
                        name = "Select using an offset",
                        test = {
                            select {
                                from("theTable")
                                offset(5)
                            }
                        },
                        expectedSql = "SELECT * FROM theTable OFFSET 5",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select using a limit",
                        test = {
                            select {
                                from("theTable")
                                limit(5)
                            }
                        },
                        expectedSql = "SELECT * FROM theTable LIMIT 5",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select using an offset and limit",
                        test = {
                            select {
                                from("theTable")
                                offset(5)
                                limit(10)
                            }
                        },
                        expectedSql = "SELECT * FROM theTable OFFSET 5 LIMIT 10",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select sorting by name",
                        test = {
                            select {
                                from("theTable")
                                sortAscending("name")
                            }
                        },
                        expectedSql = "SELECT * FROM theTable ORDER BY name ASC",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select sorting by name and age",
                        test = {
                            select {
                                from("theTable")
                                sortAscending("name")
                                sortAscending("age")
                            }
                        },
                        expectedSql = "SELECT * FROM theTable ORDER BY name ASC, age ASC",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Select sorting by name ascending and age descending",
                        test = {
                            select {
                                from("theTable")
                                sortAscending("name")
                                sortDescending("age")
                            }
                        },
                        expectedSql = "SELECT * FROM theTable ORDER BY name ASC, age DESC",
                        expectedBinds = emptyMap()
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

    /**
     * Test cases for building INSERT statements
     */
    @TestFactory
    fun testBuildInserts(): List<DynamicTest> {

        val tests = listOf(
                TestCase(
                        name = "Trivial insert",
                        test = {
                            insert("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                            }
                        },
                        expectedSql = "INSERT INTO theTable(foo, bar) VALUES (1, 'baz')",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Insert with binds",
                        test = {
                            insert("theTable") {
                                set("foo", bind(1))
                                set("bar", bind("baz"))
                            }
                        },
                        expectedSql = "INSERT INTO theTable(foo, bar) VALUES (:bv0, :bv1)",
                        expectedBinds = mapOf("bv0" to 1, "bv1" to "baz")
                ),
                TestCase(
                        name = "Insert with binds cast to a value",
                        test = {
                            insert("theTable") {
                                set("foo", cast(bind(1), "jsonb"))
                                set("bar", bind("baz"))
                            }
                        },
                        expectedSql = "INSERT INTO theTable(foo, bar) VALUES (:bv0::jsonb, :bv1)",
                        expectedBinds = mapOf("bv0" to 1, "bv1" to "baz")
                ),
                TestCase(
                        name = "Insert returning the new row",
                        test = {
                            insert("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                                returnAll()
                            }
                        },
                        expectedSql = "INSERT INTO theTable(foo, bar) VALUES (1, 'baz') RETURNING *",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Insert returning the a single field",
                        test = {
                            insert("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                                returns("foo")
                            }
                        },
                        expectedSql = "INSERT INTO theTable(foo, bar) VALUES (1, 'baz') RETURNING foo",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Insert returning the several field",
                        test = {
                            insert("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                                returns("foo", "bar")
                            }
                        },
                        expectedSql = "INSERT INTO theTable(foo, bar) VALUES (1, 'baz') RETURNING foo, bar",
                        expectedBinds = emptyMap()
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

    /**
     * Test cases for building UPDATE statements
     */
    @TestFactory
    fun testBuildUpdates(): List<DynamicTest> {

        val tests = listOf(
                TestCase(
                        name = "Trivial update",
                        test = {
                            update("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                            }
                        },
                        expectedSql = "UPDATE theTable SET foo = 1, bar = 'baz'",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Update with binds",
                        test = {
                            update("theTable") {
                                set("foo", bind(1))
                                set("bar", bind("baz"))
                            }
                        },
                        expectedSql = "UPDATE theTable SET foo = :bv0, bar = :bv1",
                        expectedBinds = mapOf("bv0" to 1, "bv1" to "baz")
                ),
                TestCase(
                        name = "Update with binds cast to a value",
                        test = {
                            update("theTable") {
                                set("foo", cast(bind(1), "jsonb"))
                                set("bar", bind("baz"))
                            }
                        },
                        expectedSql = "UPDATE theTable SET foo = :bv0::jsonb, bar = :bv1",
                        expectedBinds = mapOf("bv0" to 1, "bv1" to "baz")
                ),
                TestCase(
                        name = "Update returning the new row",
                        test = {
                            update("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                                returnAll()
                            }
                        },
                        expectedSql = "UPDATE theTable SET foo = 1, bar = 'baz' RETURNING *",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Update returning a single field",
                        test = {
                            update("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                                returns("foo")
                            }
                        },
                        expectedSql = "UPDATE theTable SET foo = 1, bar = 'baz' RETURNING foo",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Update returning several fields",
                        test = {
                            update("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                                returns("foo", "bar")
                            }
                        },
                        expectedSql = "UPDATE theTable SET foo = 1, bar = 'baz' RETURNING foo, bar",
                        expectedBinds = emptyMap()
                ),
                TestCase(
                        name = "Update with a where clause",
                        test = {
                            update("theTable") {
                                set("foo", 1)
                                set("bar", "baz")
                                where {
                                    eq(field("foo"), 2)
                                }
                            }
                        },
                        expectedSql = "UPDATE theTable SET foo = 1, bar = 'baz' WHERE (foo = 2)",
                        expectedBinds = emptyMap()
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
