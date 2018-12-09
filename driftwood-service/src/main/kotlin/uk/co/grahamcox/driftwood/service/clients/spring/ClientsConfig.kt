package uk.co.grahamcox.driftwood.service.clients.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.clients.dao.JdbcClientDao

/**
 * Spring Configuration for working with Clients
 */
@Configuration
class ClientsConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<JdbcClientDao>()
        }.initialize(context)
    }
}
