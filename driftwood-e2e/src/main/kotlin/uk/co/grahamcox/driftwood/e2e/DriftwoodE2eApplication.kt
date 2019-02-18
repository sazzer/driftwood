package uk.co.grahamcox.driftwood.e2e

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication

/**
 * Main configuration for the application
 */
@SpringBootConfiguration
@EnableAutoConfiguration
class DriftwoodE2eApplication

/**
 * Entrypoint into the application
 * @param args The commandline arguments
 */
fun main(args: Array<String>) {
    runApplication<DriftwoodE2eApplication>(*args)
}
