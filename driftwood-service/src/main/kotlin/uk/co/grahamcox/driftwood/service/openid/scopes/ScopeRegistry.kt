package uk.co.grahamcox.driftwood.service.openid.scopes

import org.slf4j.LoggerFactory

/**
 * Registry of supported scope values
 */
class ScopeRegistry(scopes: Set<Class<*>>) {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(ScopeRegistry::class.java)
    }

    /** The actual scope values */
    val scopeValues: Set<Scope>

    init {
        val invalidTypes = scopes
                .filterNot { Enum::class.java.isAssignableFrom(it) }
                .filterNot { Scope::class.java.isAssignableFrom(it) }
        if (invalidTypes.isNotEmpty()) {
            LOG.error("Provided classes do not extend both Enum and Scope: {}", invalidTypes)
            throw IllegalArgumentException("Provided classes do not extend both Enum and Scope: $invalidTypes")
        }

        scopeValues = scopes.flatMap { it.enumConstants.toList() }
                .map { it as Scope }
                .toSet()

        val ids = scopeValues.map { it.id }
                .toSet()
        if (ids.size < scopeValues.size) {
            LOG.error("Not all scopes have unique IDs")
            throw IllegalArgumentException("Not all scopes have unique IDs")
        }
        LOG.debug("Built Scope Registry: {}", scopeValues)
    }

    /**
     * Get the scope that has the given ID
     * @param id The ID to look up
     * @return the Scope, or null if not found
     */
    fun getScopeById(id: String) = scopeValues.find { it.id == id }

    /**
     * Parse a set of scope strings into the actual enum values
     * @param scopes The scopes to parse
     * @return the parsed scoped
     */
    fun parseScopes(scopes: String): Set<Scope> {
        val scopeValues = scopes.split(" ")
                .filter { it.isNotBlank() }
                .map { it to getScopeById(it) }

        val unknownIds = scopeValues.filter { it.second == null }
                .map { it.first }

        if (unknownIds.isNotEmpty()) {
            LOG.warn("Request for unknown scopes: {}", unknownIds)
            throw UnknownScopesException(unknownIds.toSet())
        }

        val result = scopeValues.filterNot { it.second == null }
                .mapNotNull { it.second }
                .toSet()
        LOG.debug("Parsed scopes {} into result set: {}", scopes, result)
        return result
    }
}
