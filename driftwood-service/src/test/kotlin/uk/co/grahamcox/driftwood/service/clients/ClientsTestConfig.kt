package uk.co.grahamcox.driftwood.service.clients

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

/**
 * Spring config for testing clients
 */
@Configuration
class ClientsTestConfig(context: GenericApplicationContext) {
    init {
        beans {
        }.initialize(context)
    }
}
