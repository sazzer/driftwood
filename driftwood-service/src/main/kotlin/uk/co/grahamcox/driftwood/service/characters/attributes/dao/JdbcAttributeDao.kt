package uk.co.grahamcox.driftwood.service.characters.attributes.dao

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
import uk.co.grahamcox.skl.SelectBuilder
import uk.co.grahamcox.skl.select
import java.sql.ResultSet

/**
 * DAO for accessing Attribute data
 * @property jdbcTemplate the JDBC Template for accessing the database
 */
@Transactional
class JdbcAttributeDao(
        private val jdbcTemplate: NamedParameterJdbcTemplate
) : AttributeRetriever {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(JdbcAttributeDao::class.java)

        /** The names of field to sort by */
        private val SORT_FIELDS = mapOf(
                AttributeSortField.NAME to "name",
                AttributeSortField.CREATED to "created",
                AttributeSortField.UPDATED to "updated"
        )
    }
    /**
     * Get the Attribute with the given ID
     * @param id The ID of the attribute
     * @return The attribute
     */
    @Transactional(readOnly = true)
    override fun getById(id: AttributeId): Resource<AttributeId, AttributeData> {
        LOG.debug("Loading Attribute with ID: {}", id)
        val query = select {
            from("attributes")
            where {
                eq(field("attribute_id"), bind(id.id))
            }
        }.build()

        val attribute = try {
            jdbcTemplate.queryForObject(query.sql, query.binds) {
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
        val query = select {
            from("attributes")
            buildListFilters(filters)
            sorts.forEach { (sortField, sortDirection) ->
                val sortFieldName = SORT_FIELDS.getValue(sortField)
                when (sortDirection) {
                    SortDirection.ASCENDING -> sortAscending(sortFieldName)
                    SortDirection.DESCENDING -> sortDescending(sortFieldName)
                }
            }
            sortAscending("attribute_id")
            offset(offset)
            limit(pageSize)
        }.build()

        val attributes = jdbcTemplate.query(query.sql, query.binds) {
            rs, _ -> parseAttributeRow(rs)
        }

        val count = if (attributes.size == 0 || attributes.size == pageSize) {
            val countQuery = select {
                from("attributes")
                returning(field("COUNT(*)"), "count")
                buildListFilters(filters)
            }.build()
            jdbcTemplate.queryForObject(countQuery.sql, countQuery.binds, Integer::class.java)?.toInt() ?: 0
        } else {
            0
        }

        return Page(attributes, offset, count)
    }

    /**
     * Build the where clause for filtering a list of attributes
     * @param filters The filters to apply
     */
    private fun SelectBuilder.buildListFilters(filters: AttributeFilters) {
        where {
            filters.name?.let { eq(function("UPPER", field("name")), function("UPPER", bind(it))) }
            filters.createdAfter?.let { where(field("created"), ">=", bind(it)) }
            filters.createdBefore?.let { where(field("created"), "<=", bind(it)) }
            filters.updatedAfter?.let { where(field("updated"), ">=", bind(it)) }
            filters.updatedBefore?.let { where(field("updated"), "<=", bind(it)) }
        }
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
