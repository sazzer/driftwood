package uk.co.grahamcox.driftwood.service.authentication

/**
 * Interface describing how to authenticate a user
 */
interface Authenticator : StartAuthenticationBuilder, ExternalUserLoader
