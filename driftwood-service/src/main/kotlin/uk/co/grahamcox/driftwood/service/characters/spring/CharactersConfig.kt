package uk.co.grahamcox.driftwood.service.characters.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import uk.co.grahamcox.driftwood.service.characters.attributes.spring.AttributesConfig

/**
 * Spring Configuration for working with Characters
 */
@Configuration
@Import(
        AttributesConfig::class
)
class CharactersConfig
