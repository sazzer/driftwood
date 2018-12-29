package uk.co.grahamcox.driftwood.service.authorization.authorizor

/**
 * The result of some authorization call
 */
sealed class AuthorizationResult

/**
 * Indication that authorization has succeeded
 */
class AuthorizationSuccessResult : AuthorizationResult()

/**
 * Indication that authorization has failed
 */
class AuthorizationFailedResult : AuthorizationResult()
