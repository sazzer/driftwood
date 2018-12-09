package uk.co.grahamcox.driftwood.service.clients.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import uk.co.grahamcox.driftwood.service.clients.ClientData
import uk.co.grahamcox.driftwood.service.clients.ClientId
import uk.co.grahamcox.driftwood.service.clients.ClientNotFoundException
import uk.co.grahamcox.driftwood.service.clients.ClientRetriever
import uk.co.grahamcox.driftwood.service.model.Resource
import java.sql.ResultSet

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
        val client = try {
            jdbcTemplate.queryForObject("SELECT * FROM clients WHERE client_id = :clientid::uuid",
                    mapOf("clientid" to id.id),
                    ::parseClientRow)!!
        } catch (e: EmptyResultDataAccessException) {
            LOG.warn("No clients found with ID: {}", id)
            throw ClientNotFoundException(id)
        }

        LOG.debug("Found clients with ID {}: {}", id, client)
        return client
    }

    /**
     * Parse the current ResultSet row into a Client resource
     * @param rs The resultset to parse from
     * @param index The index of the current row
     * @return The client
     */
    private fun parseClientRow(rs: ResultSet, index: Int): Resource<ClientId, ClientData> {
        TODO()
    }
}
