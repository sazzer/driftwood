package uk.co.grahamcox.driftwood.service.users.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.users.dao.JdbcUserDao
import uk.co.grahamcox.driftwood.service.users.rest.UserController

/**
 * Spring Configuration for working with Users
 */
@Configuration
class UsersConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<JdbcUserDao>()
            bean<UserController>()
        }.initialize(context)
    }
}
