package uk.co.grahamcox.driftwood.service.authorization.authorizor

import uk.co.grahamcox.driftwood.service.openid.token.AccessToken

/**
 * The implementation of the Authorizer to use
 * @property accessToken The access token to authorize
 */
class AuthorizerImpl(private val accessToken: AccessToken?) : Authorizer {
    /**
     * Actually perform some authorization checks
     * @param block The block that defines the checks to run
     * @return the result of authorization
     */
    override operator fun invoke(block: AuthorizationContext.() -> Unit): AuthorizationResult {
        val context = AuthorizationContextImpl(accessToken)
        block.invoke(context)

        return context.result
    }

}
