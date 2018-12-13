package uk.co.grahamcox.driftwood.service.dao

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import uk.co.grahamcox.driftwood.service.DriftwoodServiceApplication
import uk.co.grahamcox.driftwood.service.spring.DatabaseCleaner
import uk.co.grahamcox.driftwood.service.spring.TestConfig

/**
 * Base class for DAO Tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [DriftwoodServiceApplication::class])
@ActiveProfiles("test")
@Import(TestConfig::class)
@Transactional
class DaoTestBase {
    /** The database cleaner to use */
    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    /**
     * Clean the database before each test
     */
    @BeforeEach
    fun clean() {
        databaseCleaner.clean()
    }
}
