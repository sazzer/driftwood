package uk.co.grahamcox.driftwood.service.acceptance.characters.attributes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import uk.co.grahamcox.driftwood.service.acceptance.AcceptanceTestBase
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import java.util.*

/**
 * Acceptance Tests for getting attributes by ID
 */
class GetAttributeByIdAcceptanceTest : AcceptanceTestBase() {
    /** The means to seed attribute records */
    @Autowired
    private lateinit var attributeSeeder: DatabaseSeeder
    
    /** The ID of the Attribute to work with */
    private val attributeId = UUID.randomUUID().toString()

    /**
     * Create the attribute to work with
     */
    @BeforeEach
    fun createAttribute() {
        attributeSeeder(
                "attributeId" to attributeId,
                "created" to "2018-12-06T16:31:00Z",
                "updated" to "2018-12-06T16:31:00Z",
                "name" to "Strength",
                "description" to "How strong I am"
        )
    }

    /**
     * Test getting the attribute that we are logged in as
     */
    @Test
    fun getAttribute() {
        val attributeResponse = requester.get("/api/attributes/$attributeId")
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.OK, attributeResponse.statusCode) },

                Executable { attributeResponse.assertBody("""{
                    "id": "$attributeId",
                    "name": "Strength",
                    "description": "How strong I am"
                }""".trimMargin()) }
        )
    }

    /**
     * Test getting a attribute that doesn't exist
     */
    @Test
    fun getUnknownAttribute() {
        val otherAttributeId = UUID.randomUUID().toString()

        val attributeResponse = requester.get("/api/attributes/$otherAttributeId")
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.NOT_FOUND, attributeResponse.statusCode) },

                Executable { attributeResponse.assertBody("""{
                    "type": "tag:2018,grahamcox.co.uk:attributes/problems/not-found",
                    "title": "Requested attribute was not found",
                    "status": 404
                }""".trimMargin()) }
        )
    }

    /**
     * Test getting an attribute where the ID is not valid
     */
    @Test
    fun getAttributeWithInvalidId() {
        val otherUserId = "invalid"

        val userResponse = requester.get("/api/attributes/$otherUserId")
        Assertions.assertAll(
                Executable { Assertions.assertEquals(HttpStatus.BAD_REQUEST, userResponse.statusCode) },

                Executable { userResponse.assertBody("""{
                  "type" : "tag:2018,grahamcox.co.uk:problems/imvalid-argument",
                  "title" : "Request argument was not valid",
                  "status" : 400,
                  "name" : "id",
                  "value" : "invalid"
                }""".trimMargin()) }
        )
    }
}
