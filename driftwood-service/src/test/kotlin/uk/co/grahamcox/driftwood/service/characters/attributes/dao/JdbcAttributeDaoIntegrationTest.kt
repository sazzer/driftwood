package uk.co.grahamcox.driftwood.service.characters.attributes.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeFilters
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeId
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeNotFoundException
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeSortField
import uk.co.grahamcox.driftwood.service.dao.DaoTestBase
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import uk.co.grahamcox.driftwood.service.model.SortDirection
import java.time.Instant
import java.util.*

/**
 * Integration test for the JDBC Attribute DAO
 */
internal class JdbcAttributeDaoIntegrationTest : DaoTestBase() {
    companion object {
        /** The ID of the attribute to use */
        private val ATTRIBUTE_ID = AttributeId(UUID.randomUUID())
    }

    /** The means to seed attribute records */
    @Autowired
    private lateinit var attributeSeeder: DatabaseSeeder

    /** The test subject */
    @Autowired
    private lateinit var testSubject: JdbcAttributeDao

    /** The "current time" */
    @Autowired
    @Qualifier("currentTime")
    private lateinit var currentTime: Instant

    /**
     * Test getting a attribute by ID when the attribute doesn't exist
     */
    @Test
    fun getUnknownAttributeById() {
        val e = Assertions.assertThrows(AttributeNotFoundException::class.java) {
            testSubject.getById(JdbcAttributeDaoIntegrationTest.ATTRIBUTE_ID)
        }

        Assertions.assertEquals(JdbcAttributeDaoIntegrationTest.ATTRIBUTE_ID, e.id)
    }

    /**
     * Test getting a attribute by ID when the attribute does exist
     */
    @Test
    fun getAttributeById() {
        val version = UUID.randomUUID()

        attributeSeeder(
                "attributeId" to ATTRIBUTE_ID.id.toString(),
                "version" to version.toString(),
                "created" to currentTime.toString(),
                "updated" to currentTime.toString(),
                "name" to "Strength",
                "description" to "How strong I am"
        )

        val attribute = testSubject.getById(JdbcAttributeDaoIntegrationTest.ATTRIBUTE_ID)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(JdbcAttributeDaoIntegrationTest.ATTRIBUTE_ID, attribute.identity.id) },
                Executable { Assertions.assertEquals(version, attribute.identity.version) },
                Executable { Assertions.assertEquals(currentTime, attribute.identity.created) },
                Executable { Assertions.assertEquals(currentTime, attribute.identity.updated) },

                Executable { Assertions.assertEquals("Strength", attribute.data.name) },
                Executable { Assertions.assertEquals("How strong I am", attribute.data.description) }
        )
    }

    /**
     * Test various iterations where there is no data to return
     */
    @TestFactory
    fun listNoAttributes(): List<DynamicTest> {
        data class Test(
                val filters: AttributeFilters = AttributeFilters(),
                val sorts: List<Pair<AttributeSortField, SortDirection>> = emptyList(),
                val offset: Int = 0,
                val pageSize: Int = Integer.MAX_VALUE,
                val description: String
        )

        return listOf(
                Test(description = "Default values"),
                Test(pageSize = 0, description = "Page Size = 0"),
                Test(offset = 10, description = "Offset = 10"),
                Test(offset = 10, pageSize = 0, description = "Offset = 10, Page Size = 0"),
                Test(filters = AttributeFilters(name = "Unknown"), description = "Filtered by name"),
                Test(filters = AttributeFilters(createdBefore = Instant.now()), description = "Filtered by created before"),
                Test(filters = AttributeFilters(createdAfter = Instant.now()), description = "Filtered by created after"),
                Test(filters = AttributeFilters(updatedBefore = Instant.now()), description = "Filtered by updated before"),
                Test(filters = AttributeFilters(updatedAfter = Instant.now()), description = "Filtered by updated after")
        ).map { test ->
            DynamicTest.dynamicTest(test.toString()) {
                val results = testSubject.list(test.filters, test.sorts, test.offset, test.pageSize)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(test.offset, results.offset) },
                        Executable { Assertions.assertEquals(0, results.total) },
                        Executable { Assertions.assertEquals(0, results.data.size) }
                )
            }
        }
    }

    /**
     * Test various iterations where there is data but none of it is returned
     */
    @TestFactory
    fun listAttributesNoneReturned(): List<DynamicTest> {
        attributeSeeder(
                "attributeId" to ATTRIBUTE_ID.id.toString(),
                "created" to currentTime.toString(),
                "updated" to currentTime.toString(),
                "name" to "Strength",
                "description" to "How strong I am"
        )

        data class Test(
                val filters: AttributeFilters = AttributeFilters(),
                val sorts: List<Pair<AttributeSortField, SortDirection>> = emptyList(),
                val offset: Int = 0,
                val pageSize: Int = Integer.MAX_VALUE,
                val expected: Int,
                val description: String
        )

        return listOf(
                Test(pageSize = 0, expected = 1, description = "Page Size = 0"),
                Test(offset = 10, expected = 1, description = "Offset = 10"),
                Test(offset = 10, pageSize = 0, expected = 1, description = "Offset = 10, Page Size = 0"),
                Test(filters = AttributeFilters(name = "Unknown"), expected = 0, description = "Filtered by name"),
                Test(filters = AttributeFilters(createdBefore = currentTime.minusSeconds(10)), expected = 0, description = "Filtered by created before"),
                Test(filters = AttributeFilters(createdAfter = currentTime.plusSeconds(10)), expected = 0, description = "Filtered by created after"),
                Test(filters = AttributeFilters(updatedBefore = currentTime.minusSeconds(10)), expected = 0, description = "Filtered by updated before"),
                Test(filters = AttributeFilters(updatedAfter = currentTime.plusSeconds(10)), expected = 0, description = "Filtered by updated after")
        ).map { test ->
            DynamicTest.dynamicTest(test.toString()) {
                val results = testSubject.list(test.filters, test.sorts, test.offset, test.pageSize)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(test.offset, results.offset) },
                        Executable { Assertions.assertEquals(test.expected, results.total) },
                        Executable { Assertions.assertEquals(0, results.data.size) }
                )
            }
        }
    }
}
