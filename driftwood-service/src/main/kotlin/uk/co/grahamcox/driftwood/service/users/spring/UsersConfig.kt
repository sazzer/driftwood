package uk.co.grahamcox.driftwood.service.users.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.users.dao.JdbcUserDao

/**
 * Spring Configuration for working with Users
 */
@Configuration
class UsersConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<JdbcUserDao>()
        }.initialize(context)
    }
}
