package uk.co.grahamcox.driftwood.service.clients.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.clients.ClientNotFoundException
import uk.co.grahamcox.driftwood.service.clients.GrantTypes
import uk.co.grahamcox.driftwood.service.clients.ResponseTypes
import uk.co.grahamcox.driftwood.service.dao.DaoTestBase
import uk.co.grahamcox.driftwood.service.dao.DatabaseSeeder
import uk.co.grahamcox.driftwood.service.users.UserId
import java.net.URI
import java.time.Instant
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

    /** The means to seed client records */
    @Autowired
    private lateinit var clientSeeder: DatabaseSeeder

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

    /**
     * Gest getting a client by ID when the client does exist and has minimal data
     */
    @Test
    fun getMinimalClientById() {
        val version = UUID.randomUUID()
        val now = Instant.parse("2018-12-06T16:31:00Z")

        clientSeeder(
                "clientId" to CLIENT_ID.id.toString(),
                "version" to version.toString(),
                "created" to now.toString(),
                "updated" to now.toString(),
                "name" to "Example Client",
                "ownerId" to USER_ID.id.toString(),
                "redirectUris" to "",
                "responseTypes" to "",
                "grantTypes" to ""
        )

        val client = testSubject.getById(CLIENT_ID)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(CLIENT_ID, client.identity.id) },
                Executable { Assertions.assertEquals(version, client.identity.version) },
                Executable { Assertions.assertEquals(now, client.identity.created) },
                Executable { Assertions.assertEquals(now, client.identity.updated) },

                Executable { Assertions.assertEquals("Example Client", client.data.name) },
                Executable { Assertions.assertEquals(USER_ID, client.data.owner) },
                Executable { Assertions.assertEquals(emptySet<URI>(), client.data.redirectUris) },
                Executable { Assertions.assertEquals(emptySet<ResponseTypes>(), client.data.responseTypes) },
                Executable { Assertions.assertEquals(emptySet<GrantTypes>(), client.data.grantTypes) }
        )
    }

    /**
     * Gest getting a client by ID when the client does exist and has minimal data
     */
    @Test
    fun getCompleteClientById() {
        val version = UUID.randomUUID()
        val now = Instant.parse("2018-12-06T16:31:00Z")

        clientSeeder(
                "clientId" to CLIENT_ID.id.toString(),
                "version" to version.toString(),
                "created" to now.toString(),
                "updated" to now.toString(),
                "name" to "Example Client",
                "ownerId" to USER_ID.id.toString(),
                "redirectUris" to "http://www.google.com,http://www.example.com",
                "responseTypes" to "code,token",
                "grantTypes" to "implicit,authorization_code"
        )

        val client = testSubject.getById(CLIENT_ID)

        Assertions.assertAll(
                Executable { Assertions.assertEquals(CLIENT_ID, client.identity.id) },
                Executable { Assertions.assertEquals(version, client.identity.version) },
                Executable { Assertions.assertEquals(now, client.identity.created) },
                Executable { Assertions.assertEquals(now, client.identity.updated) },

                Executable { Assertions.assertEquals("Example Client", client.data.name) },
                Executable { Assertions.assertEquals(USER_ID, client.data.owner) },
                Executable { Assertions.assertEquals(setOf(
                        URI("http://www.google.com"),
                        URI("http://www.example.com")
                ), client.data.redirectUris) },
                Executable { Assertions.assertEquals(setOf(ResponseTypes.CODE, ResponseTypes.TOKEN), client.data.responseTypes) },
                Executable { Assertions.assertEquals(setOf(GrantTypes.IMPLICIT, GrantTypes.AUTHORIZATION_CODE), client.data.grantTypes) }
        )
    }
}
