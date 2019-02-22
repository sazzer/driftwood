package uk.co.grahamcox.driftwood.e2e.browser

import cucumber.api.java8.En
import org.junit.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Cucumber step class to ensure that Spring is started
 */
@SpringBootTest()
class BrowserSteps : En {
    /** The browser */
    @Autowired
    private lateinit var browser: Browser

    init {
        Then("the page title should be {string}") { title: String ->
            Assert.assertEquals(title, browser.title)
        }
    }
}

