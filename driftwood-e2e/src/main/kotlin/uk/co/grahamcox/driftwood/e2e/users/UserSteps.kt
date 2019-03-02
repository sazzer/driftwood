package uk.co.grahamcox.driftwood.e2e.users

import cucumber.api.java8.En
import io.cucumber.datatable.DataTable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import uk.co.grahamcox.driftwood.e2e.database.DatabaseSeeder

/**
 * Cucumber steps for working with users
 */
class UserSteps : En {
    /** The User Seeder */
    @Autowired
    @Qualifier("userSeeder")
    private lateinit var userSeeder: DatabaseSeeder

    init {
        Given("a user exists with details:") { dataTable: DataTable ->
            dataTable.transpose().asMaps().forEach(userSeeder::seed)
        }
    }
}
