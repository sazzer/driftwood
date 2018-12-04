package uk.co.grahamcox.driftwood.service.spring

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * The main Spring configuration
 */
@Configuration
@Import(
        DatabaseConfig::class
)
class DriftwoodConfig
