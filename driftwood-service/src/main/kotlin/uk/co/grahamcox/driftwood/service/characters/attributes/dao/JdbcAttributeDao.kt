package uk.co.grahamcox.driftwood.service.characters.attributes.dao

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeData
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeId
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeNotFoundException
import uk.co.grahamcox.driftwood.service.characters.attributes.AttributeRetriever
import uk.co.grahamcox.driftwood.service.database.getUUID
import uk.co.grahamcox.driftwood.service.model.Identity
import uk.co.grahamcox.driftwood.service.model.Resource
import java.sql.ResultSet
import java.time.Clock

/**
 * DAO for accessing Attribute data
 * @property jdbcTemplate the JDBC Template for accessing the database
 */
@Transactional
class JdbcAttributeDao(
        private val jdbcTemplate: NamedParameterJdbcTemplate,
        private val objectMapper: ObjectMapper,
        private val clock: Clock
) : AttributeRetriever {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(JdbcAttributeDao::class.java)
    }
    /**
     * Get the Attribute with the given ID
     * @param id The ID of the attribute
     * @return The attribute
     */
    @Transactional(readOnly = true)
    override fun getById(id: AttributeId): Resource<AttributeId, AttributeData> {
        LOG.debug("Loading Attribute with ID: {}", id)
        val attribute = try {
            jdbcTemplate.queryForObject("SELECT * FROM attributes WHERE attribute_id = :attributeid::uuid",
                    mapOf("attributeid" to id.id)) {
                rs, _ -> parseAttributeRow(rs)
            }!!
        } catch (e: EmptyResultDataAccessException) {
            LOG.warn("No attribute found with ID: {}", id)
            throw AttributeNotFoundException(id)
        }

        LOG.debug("Found attribute with ID {}: {}", id, attribute)
        return attribute
    }

    /**
     * Parse the current ResultSet row into a Attribute resource
     * @param rs The resultset to parse from
     * @return The attribute
     */
    private fun parseAttributeRow(rs: ResultSet): Resource<AttributeId, AttributeData> {
        val identity = Identity(
                id = AttributeId(rs.getUUID("attribute_id")),
                version = rs.getUUID("version"),
                created = rs.getTimestamp("created").toInstant(),
                updated = rs.getTimestamp("updated").toInstant()
        )

        val data = AttributeData(
                name = rs.getString("name"),
                description = rs.getString("description")
        )

        return Resource(identity, data)
    }
}
