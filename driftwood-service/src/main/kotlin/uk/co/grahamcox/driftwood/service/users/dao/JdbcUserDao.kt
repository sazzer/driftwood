package uk.co.grahamcox.driftwood.service.users.dao

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.driftwood.service.dao.OptimisticLockException
import uk.co.grahamcox.driftwood.service.database.getUUID
import uk.co.grahamcox.driftwood.service.model.Identity
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.users.*
import uk.co.grahamcox.skl.insert
import uk.co.grahamcox.skl.select
import java.sql.ResultSet
import java.time.Clock
import java.util.*

/**
 * DAO for accessing User data
 * @property jdbcTemplate the JDBC Template for accessing the database
 */
@Transactional
class JdbcUserDao(
        private val jdbcTemplate: NamedParameterJdbcTemplate,
        private val objectMapper: ObjectMapper,
        private val clock: Clock
) : UserService {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(JdbcUserDao::class.java)
    }
    /**
     * Get the User with the given ID
     * @param id The ID of the user
     * @return The user
     */
    @Transactional(readOnly = true)
    override fun getById(id: UserId): Resource<UserId, UserData> {
        LOG.debug("Loading User with ID: {}", id)
        val query = select {
            from("users")
            where {
                eq(field("user_id"), bind(id.id))
            }
        }.build()

        val user = try {
            jdbcTemplate.queryForObject(query.sql, query.binds) {
                rs, _ -> parseUserRow(rs)
            }!!
        } catch (e: EmptyResultDataAccessException) {
            LOG.warn("No user found with ID: {}", id)
            throw UserNotFoundException(id)
        }

        LOG.debug("Found user with ID {}: {}", id, user)
        return user
    }

    /**
     * Get the User with the given ID in the given External Provider
     * @param provider The Provider that the ID belongs to
     * @param id The ID of the user
     * @return The user, or null if one isn't found
     */
    override fun getByProviderId(provider: String, id: String): Resource<UserId, UserData>? {
        LOG.debug("Loading User with ID {} at provider {}", id, provider)

        val providerParam = objectMapper.writeValueAsString(
                listOf(mapOf("provider" to provider, "providerId" to id))
        )
        val query = select {
            from("users")
            where {
                where(field("authentication"), "@>", cast(bind(providerParam), "jsonb"))
            }
        }.build()


        val user = try {

            jdbcTemplate.queryForObject(query.sql, query.binds) {
                rs, _ -> parseUserRow(rs)
            }
        } catch (e: EmptyResultDataAccessException) {
            LOG.debug("No user found with ID {} at provider {}", id, provider)
            null
        }

        LOG.debug("Found user with ID {} at provider {}: {}", id, provider, user)
        return user
    }

    /**
     * Save changes to an existing user
     * @param user The new user details
     * @return the updated user details
     */
    @Transactional(readOnly = false)
    override fun save(user: Resource<UserId, UserData>): Resource<UserId, UserData> {
        LOG.debug("Saving User with ID: {}", user.identity.id)

        val updated = try {
            jdbcTemplate.queryForObject("""
                UPDATE
                    users
                SET
                    version = :newVersion::uuid,
                    updated = :newUpdated,
                    name = :newName,
                    email = :newEmail,
                    authentication = :newAuthentication::jsonb
                WHERE
                    user_id = :userid::uuid
                    AND version = :version::uuid
                RETURNING
                    *
                """,
                    mapOf(
                            "userid" to user.identity.id.id,
                            "version" to user.identity.version,
                            "newVersion" to UUID.randomUUID(),
                            "newUpdated" to Date.from(clock.instant()),
                            "newName" to user.data.name,
                            "newEmail" to user.data.email,
                            "newAuthentication" to objectMapper.writeValueAsString(user.data.logins)
                    )) { rs, _ -> parseUserRow(rs) }!!
        } catch (e: EmptyResultDataAccessException) {
            val query = select {
                from("users")
                returning(field("version"))
                where {
                    eq(field("user_id"), bind(user.identity.id.id))
                }
            }.build()

            try {
                val currentVersion = jdbcTemplate.queryForObject(query.sql, query.binds) { rs, _ ->
                    rs.getUUID("version")
                }!!

                LOG.warn("User found with ID {}, but database version {} didn't match provided version {}",
                        user.identity.id, currentVersion, user.identity.version)
                throw OptimisticLockException(user.identity.id, currentVersion)
            } catch (e: EmptyResultDataAccessException) {
                LOG.warn("No user found with ID: {}", user.identity.id)
                throw UserNotFoundException(user.identity.id)
            }
        }

        LOG.debug("Updated user with ID {}: {}", updated.identity.id, updated)
        return updated
    }

    /**
     * Save a new user
     * @param user The new user details
     * @return the new user details
     */
    override fun save(user: UserData): Resource<UserId, UserData> {
        LOG.debug("Creating user: {}", user)

        val query = insert("users") {
            set("user_id", bind(UUID.randomUUID()))
            set("version", bind(UUID.randomUUID()))
            set("created", bind(Date.from(clock.instant())))
            set("updated", bind(Date.from(clock.instant())))
            set("name", bind(user.name))
            set("email", bind(user.email))
            set("authentication", cast(bind(objectMapper.writeValueAsString(user.logins)), "jsonb"))
            returnAll()
        }.build()

        val savedUser = jdbcTemplate.queryForObject(query.sql, query.binds) { rs, _ ->
            parseUserRow(rs)
        }!!

        LOG.debug("Created user: {}", savedUser)

        return savedUser
    }

    /**
     * Parse the current ResultSet row into a User resource
     * @param rs The resultset to parse from
     * @return The user
     */
    private fun parseUserRow(rs: ResultSet): Resource<UserId, UserData> {
        val identity = Identity(
                id = UserId(rs.getUUID("user_id")),
                version = rs.getUUID("version"),
                created = rs.getTimestamp("created").toInstant(),
                updated = rs.getTimestamp("updated").toInstant()
        )

        val providersString = rs.getString("authentication")
        val providersType = objectMapper.typeFactory.constructCollectionType(Set::class.java, UserLoginData::class.java)
        val providers: Set<UserLoginData> = objectMapper.readValue(providersString, providersType)

        val data = UserData(
                name = rs.getString("name"),
                email = rs.getString("email"),
                logins = providers
        )

        return Resource(identity, data)
    }
}
