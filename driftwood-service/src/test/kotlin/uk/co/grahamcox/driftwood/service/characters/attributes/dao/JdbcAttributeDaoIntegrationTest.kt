package uk.co.grahamcox.driftwood.service.characters.attributes.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeId
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeNotFoundException
import uk.co.grahamcox.driftwood.service.dao.DaoTestBase
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
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
                "attributeId" to JdbcAttributeDaoIntegrationTest.ATTRIBUTE_ID.id.toString(),
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
}
