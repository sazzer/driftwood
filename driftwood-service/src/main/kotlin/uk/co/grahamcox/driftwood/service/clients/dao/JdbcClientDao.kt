package uk.co.grahamcox.driftwood.service.clients.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import uk.co.grahamcox.driftwood.service.clients.*
import uk.co.grahamcox.driftwood.service.database.getStringArray
import uk.co.grahamcox.driftwood.service.model.Identity
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.users.UserId
import uk.co.grahamcox.skl.select
import java.net.URI
import java.sql.ResultSet
import java.util.*

/**
 * DAO for accessing Client data
 * @property jdbcTemplate the JDBC Template for accessing the database
 */
class JdbcClientDao(
        private val jdbcTemplate: NamedParameterJdbcTemplate
) : ClientRetriever {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(JdbcClientDao::class.java)
    }

    /**
     * Get the Client with the given ID
     * @param id The ID of the clients
     * @return The clients
     */
    override fun getById(id: ClientId): Resource<ClientId, ClientData> {
        LOG.debug("Loading Client with ID: {}", id)
        val query = select {
            from("clients")
            where {
                eq(field("client_id"), bind(id.id))
            }
        }.build()

        val client = try {
            jdbcTemplate.queryForObject(query.sql, query.binds) {
                rs, _ -> parseClientRow(rs)
            }!!
        } catch (e: EmptyResultDataAccessException) {
            LOG.warn("No client found with ID: {}", id)
            throw ClientNotFoundException(id)
        }

        LOG.debug("Found client with ID {}: {}", id, client)
        return client
    }

    /**
     * Get the Client with the given ID and Secret
     * @param id The ID of the client
     * @param secret The Secret of the client
     * @return The clients
     */
    override fun getByCredentials(id: ClientId, secret: ClientSecret): Resource<ClientId, ClientData> {
        LOG.debug("Loading Client with ID: {}", id)
        val query = select {
            from("clients")
            where {
                eq(field("client_id"), bind(id.id))
                eq(field("client_secret"), bind(secret.id))
            }
        }.build()

        val client = try {
            jdbcTemplate.queryForObject(query.sql, query.binds) {
                rs, _ -> parseClientRow(rs)
            }!!
        } catch (e: EmptyResultDataAccessException) {
            LOG.warn("No client found with ID: {}", id)
            throw ClientNotFoundException(id)
        }

        LOG.debug("Found client with ID {}: {}", id, client)
        return client
    }

    /**
     * Parse the current ResultSet row into a Client resource
     * @param rs The resultset to parse from
     * @return The client
     */
    private fun parseClientRow(rs: ResultSet): Resource<ClientId, ClientData> {
        val identity = Identity(
                id = ClientId(UUID.fromString(rs.getString("client_id"))),
                version = UUID.fromString(rs.getString("version")),
                created = rs.getTimestamp("created").toInstant(),
                updated = rs.getTimestamp("updated").toInstant()
        )

        val data = ClientData(
                name = rs.getString("name"),
                owner = UserId(UUID.fromString(rs.getString("owner_id"))),
                secret = ClientSecret(UUID.fromString(rs.getString("client_secret"))),
                redirectUris = rs.getStringArray("redirect_uris")
                        .map(::URI)
                        .toSet(),
                responseTypes = rs.getStringArray("response_types")
                        .map(ResponseTypes.Companion::getByValue)
                        .filterNotNull()
                        .toSet(),
                grantTypes = rs.getStringArray("grant_types")
                        .map(GrantTypes.Companion::getByValue)
                        .filterNotNull()
                        .toSet()
        )

        return Resource(identity, data)
    }
}
