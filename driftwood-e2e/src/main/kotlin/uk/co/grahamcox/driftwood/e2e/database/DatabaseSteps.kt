package uk.co.grahamcox.driftwood.e2e.database

import cucumber.api.java8.En
import org.springframework.beans.factory.annotation.Autowired

/**
 * Cucumber steps for the database interaction
 */
class DatabaseSteps : En {
    /** The database cleaner */
    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    init {
        Before { scenario -> databaseCleaner.clean() }
    }
}
