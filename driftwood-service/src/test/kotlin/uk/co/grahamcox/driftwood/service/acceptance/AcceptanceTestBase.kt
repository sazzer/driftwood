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

/**
 * Base class for all Acceptance Tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(
        AcceptanceConfig::class
)
class AcceptanceTestBase {
    /** The means to make API calls */
    @Autowired
    protected lateinit var requester: Requester

    /**
     * Reset the system before each test
     */
    @BeforeEach
    fun reset() {
        requester.reset()
    }
}
