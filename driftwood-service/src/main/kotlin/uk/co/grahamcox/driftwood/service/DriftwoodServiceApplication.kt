package uk.co.grahamcox.driftwood.service

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import uk.co.grahamcox.driftwood.service.spring.DriftwoodConfig

/**
 * Main configuration for the application
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(DriftwoodConfig::class)
class DriftwoodServiceApplication

/**
 * Entrypoint into the application
 * @param args The commandline arguments
 */
fun main(args: Array<String>) {
    runApplication<DriftwoodServiceApplication>(*args)
}
