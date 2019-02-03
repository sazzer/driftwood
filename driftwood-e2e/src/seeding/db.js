const process = require('process');
const { Pool, Client } = require('pg')
const getConfig = require('../config');

/** The base URL to use */
const connectionString = getConfig('DRIFTWOOD_PG_URL');

console.log('Connecting to database: %s', connectionString);

/** The connection pool to use */
const pool = new Pool({
    connectionString: connectionString,
});

/** On an error, kill the process */
pool.on('error', (err, client) => {
    console.error('Unexpected error on idle client', err)
    process.exit(-1)
});

/**
 * Actually perform a query and return the results
 * @param sql the SQL to execute
 * @param binds the binds to use, if any
 */
async function query(sql, binds) {
    console.log("Running query: %s", sql);

    const results = await pool.query(sql, binds);
    return results.rows;
}

/**
 * Actually perform a query and return the indication of changes
 * @param sql the SQL to execute
 * @param binds the binds to use, if any
 */
async function update(sql, binds) {
    console.log("Running query: %s", sql);

    const results = await pool.query(sql, binds);
    return results.rowCount;
}

module.exports = {
    query,
    update,
};
