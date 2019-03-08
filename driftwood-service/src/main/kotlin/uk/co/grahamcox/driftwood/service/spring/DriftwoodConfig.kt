package uk.co.grahamcox.driftwood.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import uk.co.grahamcox.driftwood.service.VersionController
import uk.co.grahamcox.driftwood.service.authentication.spring.AuthenticationConfig
import uk.co.grahamcox.driftwood.service.authorization.spring.AuthorizationConfig
import uk.co.grahamcox.driftwood.service.characters.spring.CharactersConfig
import uk.co.grahamcox.driftwood.service.clients.spring.ClientsConfig
import uk.co.grahamcox.driftwood.service.openid.spring.OpenIDConfig
import uk.co.grahamcox.driftwood.service.rest.problem.ProblemResponseBodyAdvice
import uk.co.grahamcox.driftwood.service.users.spring.UsersConfig
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.util.*

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
        CharactersConfig::class,
        WebMvcConfig::class
)
class DriftwoodConfig(context: GenericApplicationContext) {
    init {
        beans {
            val now = Instant.now().with(ChronoField.MILLI_OF_SECOND, 0)
            bean("currentTime") { now }
            bean {
                val activeProfiles = context.environment.activeProfiles.toList()
                if (activeProfiles.contains("test")) {
                    val currentTime = context.getBean("currentTime", Instant::class.java)
                    Clock.fixed(currentTime, ZoneId.of("UTC"))
                } else {
                    Clock.systemUTC()
                }
            }

            bean {
                val restTemplate = RestTemplate()
                restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()
                restTemplate
            }
            bean<VersionController>()
            bean<ProblemResponseBodyAdvice>()
        }.initialize(context)
    }
}
