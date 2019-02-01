require('dotenv').config({ debug: true })

/**
 * Helper to get the configuration with the given key
 * @param key they key
 * @return the config value
 */
function getConfig(key) {
    return process.env[key];
}

module.exports = getConfig;
