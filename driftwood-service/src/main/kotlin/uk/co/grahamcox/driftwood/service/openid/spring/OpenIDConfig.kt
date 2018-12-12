package uk.co.grahamcox.driftwood.service.openid.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.openid.rest.ClientCredentialsArgumentResolver
import uk.co.grahamcox.driftwood.service.openid.rest.TokenController

/**
 * Spring configuration for the OAuth2 and OpenID Connect systems
 */
@Configuration
class OpenIDConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<ClientCredentialsArgumentResolver>()
            bean<TokenController>()
        }.initialize(context)
    }
}
