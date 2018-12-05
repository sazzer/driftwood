package uk.co.grahamcox.driftwood.service.acceptance.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.acceptance.requester.Requester

/**
 * Spring config for the Acceptance tests
 */
@Configuration
class AcceptanceConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<Requester>()
        }.initialize(context)
    }
}
