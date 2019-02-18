package uk.co.grahamcox.driftwood.e2e

import cucumber.api.java8.En
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

/**
 * Cucumber step class to ensure that Spring is started
 */
@SpringBootTest()
class CucumberTestBase : En {
    companion object {
        private val LOG = LoggerFactory.getLogger(javaClass)
    }
    init {
        Before { scenario ->
            LOG.debug("Starting...")
        }
    }
}
