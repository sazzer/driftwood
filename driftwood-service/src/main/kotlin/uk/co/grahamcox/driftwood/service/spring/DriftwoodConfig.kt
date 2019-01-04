package uk.co.grahamcox.driftwood.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.VersionController
import uk.co.grahamcox.driftwood.service.authentication.spring.AuthenticationConfig
import uk.co.grahamcox.driftwood.service.authorization.spring.AuthorizationConfig
import uk.co.grahamcox.driftwood.service.clients.spring.ClientsConfig
import uk.co.grahamcox.driftwood.service.openid.spring.OpenIDConfig
import uk.co.grahamcox.driftwood.service.rest.problem.ProblemResponseBodyAdvice
import uk.co.grahamcox.driftwood.service.users.spring.UsersConfig
import java.time.Clock

/**
 * The main Spring configuration
 */
@Configuration
@Import(
        DatabaseConfig::class,
        UsersConfig::class,
        ClientsConfig::class,
        AuthorizationConfig::class,
        OpenIDConfig::class,
        AuthenticationConfig::class,
        WebMvcConfig::class
)
class DriftwoodConfig(context: GenericApplicationContext) {
    init {
        beans {
            profile("!test") {
                bean { Clock.systemUTC() }
            }
            bean<VersionController>()
            bean<ProblemResponseBodyAdvice>()
        }.initialize(context)
    }
}
