package uk.co.grahamcox.driftwood.service.characters.attributes.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.driftwood.service.characters.attributes.dao.JdbcAttributeDao

/**
 * Spring Configuration for working with Attributes
 */
@Configuration
class AttributesConfig(context: GenericApplicationContext) {
    init {
        beans {
            bean<JdbcAttributeDao>()
        }.initialize(context)
    }
}
