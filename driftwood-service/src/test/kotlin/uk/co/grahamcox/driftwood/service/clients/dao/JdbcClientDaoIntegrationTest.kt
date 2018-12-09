package uk.co.grahamcox.driftwood.service.clients.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.clients.ClientNotFoundException
import uk.co.grahamcox.driftwood.service.dao.DaoTestBase
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import uk.co.grahamcox.driftwood.service.users.UserId
import java.util.*

/**
 * Integration Tests for teh Client DAO
 */
internal class JdbcClientDaoIntegrationTest : DaoTestBase() {
    companion object {
        /** The ID of the user to use */
        private val USER_ID = UserId(UUID.randomUUID())

        /** The ID of the client to use */
        private val CLIENT_ID = ClientId(UUID.randomUUID())
    }

    /** The means to seed user records */
    @Autowired
    private lateinit var userSeeder: DatabaseSeeder

    /** The test subject */
    @Autowired
    private lateinit var testSubject: JdbcClientDao

    /**
     * Create a user to own our Client records
     */
    @BeforeEach
    fun createUser() {
        userSeeder(
                "userId" to USER_ID.id.toString()
        )
    }

    /**
     * Gest getting a client by ID when the client doesn't exist
     */
    @Test
    fun getUnknownClientById() {
        val e = Assertions.assertThrows(ClientNotFoundException::class.java) {
            testSubject.getById(CLIENT_ID)
        }

        Assertions.assertEquals(CLIENT_ID, e.id)
    }
}
