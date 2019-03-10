package uk.co.grahamcox.driftwood.service.characters.attributes.dao

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.driftwood.service.characters.attributes.*
import uk.co.grahamcox.driftwood.service.database.getUUID
import uk.co.grahamcox.driftwood.service.model.Identity
import uk.co.grahamcox.driftwood.service.model.Page
import uk.co.grahamcox.driftwood.service.model.Resource
import uk.co.grahamcox.driftwood.service.model.SortDirection
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
     * Get a list of all the matching attributes
     * @param filters The filters to apply to the list
     * @param sorts The sorts to apply to the list
     * @param offset The offset of the first attribute
     * @param pageSize The page size to return
     */
    override fun list(filters: AttributeFilters,
                      sorts: List<Pair<AttributeSortField, SortDirection>>,
                      offset: Int,
                      pageSize: Int): Page<AttributeId, AttributeData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
