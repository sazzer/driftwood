package uk.co.grahamcox.driftwood.service.openid.scopes

import java.lang.RuntimeException

/**
 * Exception to indicate that some scopes are unknown
 * @property scopes The unknown scopes
 */
class UnknownScopesException(val scopes: Set<String>) : RuntimeException("Unknown scopes: $scopes")
