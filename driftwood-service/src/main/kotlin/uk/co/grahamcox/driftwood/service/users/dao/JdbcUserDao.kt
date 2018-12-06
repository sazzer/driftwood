package uk.co.grahamcox.driftwood.service.users.dao

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import uk.co.grahamcox.driftwood.service.model.Identity
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.users.UserData
import uk.co.grahamcox.driftwood.service.users.UserId
import uk.co.grahamcox.driftwood.service.users.UserNotFoundException
import uk.co.grahamcox.driftwood.service.users.UserRetriever
import java.sql.ResultSet
import java.util.*

/**
 * DAO fro accessing User data
 * @property jdbcTemplate the JDBC Template for accessing the database
 */
class JdbcUserDao(private val jdbcTemplate: NamedParameterJdbcTemplate) : UserRetriever {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(JdbcUserDao::class.java)
    }
    /**
     * Get the User with the given ID
     * @param id The ID of the user
     * @return The user
     */
    override fun getById(id: UserId): Resource<UserId, UserData> {
        LOG.debug("Loading User with ID: {}", id)
        val user = try {
            jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = :userid::uuid",
                    mapOf("userid" to id.id),
                    ::parseUserRow)!!
        } catch (e: EmptyResultDataAccessException) {
            LOG.warn("No user found with ID: {}", id)
            throw UserNotFoundException(id)
        }

        LOG.debug("Found user with ID {}: {}", id, user)
        return user
    }

    /**
     * Parse the current ResultSet row into a User resource
     * @param rs The resultset to parse from
     * @param index The index of the current row
     * @return The user
     */
    private fun parseUserRow(rs: ResultSet, index: Int): Resource<UserId, UserData> {
        val identity = Identity(
                id = UserId(UUID.fromString(rs.getString("user_id"))),
                version = UUID.fromString(rs.getString("version")),
                created = rs.getTimestamp("created").toInstant(),
                updated = rs.getTimestamp("updated").toInstant()
        )

        val data = UserData(
                name = rs.getString("name"),
                email = rs.getString("email"),
                logins = emptySet()
        )

        return Resource(identity, data)
    }
}
