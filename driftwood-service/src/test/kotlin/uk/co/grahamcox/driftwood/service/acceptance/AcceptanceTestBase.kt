package uk.co.grahamcox.driftwood.service.acceptance

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import uk.co.grahamcox.driftwood.service.acceptance.requester.Requester
import uk.co.grahamcox.driftwood.service.acceptance.spring.AcceptanceConfig
import uk.co.grahamcox.driftwood.service.spring.DatabaseCleaner
import uk.co.grahamcox.driftwood.service.spring.TestConfig

/**
 * Base class for all Acceptance Tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(
        TestConfig::class,
        AcceptanceConfig::class
)
open class AcceptanceTestBase {
    /** The means to make API calls */
    @Autowired
    protected lateinit var requester: Requester

    /** The database cleaner to use */
    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    /**
     * Reset the system before each test
     */
    @BeforeEach
    fun reset() {
        databaseCleaner.clean()
        requester.reset()
    }
}
