package uk.co.grahamcox.driftwood.e2e.browser

import cucumber.api.Scenario
import cucumber.api.java8.En
import org.junit.Assert
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Cucumber step class to ensure that Spring is started
 */
@SpringBootTest()
class BrowserSteps : En {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(BrowserSteps::class.java)
    }

    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    /** The path in which to save screenshots */
    @Value("\${selenium.screenshotPath}")
    private lateinit var screenshotPath: String

    init {
        Before { body: Scenario ->
            browser.reset()
        }

        After { scenario: Scenario ->
            val featurePart = scenario.uri
                    .replace("uk/co/grahamcox/driftwood/e2e/features", "")
                    .replace(".feature$".toRegex(), "")
            val scenarioPart = scenario.name.replace("[^A-Za-z0-9_-]".toRegex(), "_") +
                    "-" + scenario.status.name.toUpperCase() +
                    ".png"

            LOG.debug("Saving screenshots to {} {} {}", screenshotPath, featurePart, scenarioPart)
            Files.createDirectories(Paths.get(screenshotPath, featurePart))

            browser.saveScreenshot(Paths.get(screenshotPath, featurePart, scenarioPart).toString())
        }

        Then("the page title should be {string}") { title: String ->
            Assert.assertEquals(title, browser.title)
        }
    }
}

