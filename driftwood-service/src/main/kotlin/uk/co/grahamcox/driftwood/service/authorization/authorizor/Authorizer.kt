package uk.co.grahamcox.driftwood.service.authorization.authorizor

/**
 * Interface describing how to authorize a request
 */
interface Authorizer {
    /**
     * Actually perform some authorization checks
     * @param block The block that defines the checks to run
     * @return the result of authorization
     */
    operator fun invoke(block: AuthorizationContext.() -> Unit): AuthorizationResult
}
