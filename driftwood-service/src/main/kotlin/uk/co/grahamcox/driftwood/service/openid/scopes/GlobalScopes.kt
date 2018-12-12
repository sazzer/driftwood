package uk.co.grahamcox.driftwood.service.openid.scopes

/**
 * Scopes to represent the global states
 */
enum class GlobalScopes(override val id: String) : Scope {
    /** All applicable scopes */
    ALL("*")
}
