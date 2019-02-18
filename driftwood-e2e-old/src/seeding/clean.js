const debug = require('debug')('driftwood');
const {Before} = require('cucumber');
const {query, update} = require('./db');

/** Query to get the list of tables */
const LIST_TABLES_QUERY = "SELECT table_name FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema = 'public'";

const IGNORE_TABLES = ['flyway_schema_history', 'clients', 'users'];

/**
 * Before each scenario, we empty out the database.
 * Note that we do *not* empty the clients or users tables, because there is special handling there.
 * The E2E tests do nothing with Clients, and there is one Client and one User that we can not delete.
 */
Before(async function() {
    debug('Cleaning database');

    const tables = await query(LIST_TABLES_QUERY);
    const tablesToClean = tables.map(table => table.table_name)
        .filter(table => IGNORE_TABLES.indexOf(table) === -1);

    if (tablesToClean.length > 0) {
        const truncateQuery = 'TRUNCATE ' + tablesToClean.join(', ');
        await update(truncateQuery);
    }

    // Now special handling for the users table
    const usersDeleted = await update('DELETE FROM users WHERE user_id NOT IN (SELECT owner_id FROM clients)');
    debug('Deleted %s users', usersDeleted);
});
