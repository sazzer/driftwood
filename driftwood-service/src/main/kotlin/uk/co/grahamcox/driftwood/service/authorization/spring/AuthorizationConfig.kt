package uk.co.grahamcox.driftwood.service.authorization.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.authorization.AccessTokenStore
import uk.co.grahamcox.driftwood.service.authorization.rest.AccessTokenArgumentResolver
import uk.co.grahamcox.driftwood.service.authorization.rest.AccessTokenInterceptor
import uk.co.grahamcox.driftwood.service.authorization.rest.AuthorizationControllerAdvice
import uk.co.grahamcox.driftwood.service.authorization.rest.DebugController

/**
 * Spring configuration for the Authorization systems
 */
@Configuration
class AuthorizationConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<AccessTokenStore>()
            bean<AccessTokenInterceptor>()
            bean<AccessTokenArgumentResolver>()
            bean<AuthorizationControllerAdvice>()
            bean<DebugController>()
        }.initialize(context)
    }
}
