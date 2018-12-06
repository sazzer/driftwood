package uk.co.grahamcox.driftwood.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import uk.co.grahamcox.driftwood.service.spring.DatabaseCleaner
import uk.co.grahamcox.driftwood.service.spring.TestConfig

/**
 * Base class for DAO Tests
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestConfig::class)
class DaoTestBase {
    /** Construct the JDBC Template */
    @Autowired
    protected lateinit var jdbcTemplate: NamedParameterJdbcTemplate

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
