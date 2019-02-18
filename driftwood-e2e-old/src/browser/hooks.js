const debug = require('debug')('driftwood');
const fs = require('fs');
const {After, AfterAll, BeforeAll, Before, Status} = require('cucumber');
const {Builder} = require('selenium-webdriver');
const logging = require('selenium-webdriver/lib/logging');
const Browser = require('./Browser');

let driver;

BeforeAll(async () => {
    const loggingPrefs = new logging.Preferences();
    loggingPrefs.setLevel(logging.Type.BROWSER, logging.Level.DEBUG);

    driver = new Builder()
        .forBrowser('chrome')
        .setLoggingPrefs(loggingPrefs)
        .build();
    debug('Created web browser');
});

Before(function() {
    driver.executeScript('window.sessionStorage.clear();')
    this._browser = new Browser(driver);

});

After(async function(testCase) {
    if (!fs.existsSync('screenshots')) {
        fs.mkdirSync('screenshots', {recursive: true});
    }

    const featureFile = testCase.sourceLocation.uri;
    const scenario = testCase.pickle.name;

    const fileFeatureBase = featureFile.replace(/^features\//, '')
        .replace(/\.feature$/, '')
        .replace(/[^A-Za-z0-9_-]/g, '_');
    const fileScenarioBase = scenario.replace(/[^A-Za-z0-9_-]/g, '_');

    const fileBase = `screenshots/${fileFeatureBase}_${fileScenarioBase}`;

    debug('Writing screenshot to file: %s', fileBase + '.png');
    const image = await driver.takeScreenshot();
    fs.writeFileSync(`${fileBase}.png`, image, 'base64');

    debug('Writing browser log to file: %s', fileBase + '.log');
    const logs = await driver.manage().logs().get(logging.Type.BROWSER);
    const browserLog = logs.map(entry => `[${entry.level.name}] ${entry.message}`)
        .join('\n');
    fs.writeFileSync(`${fileBase}.log`, browserLog);
});

AfterAll(async function() {
    await driver.quit();
    debug('Quit web browser');
});

/**
 * Wait for the given predicate to come true
 * @param until the predicate
 */
async function wait(until) {
    await driver.wait(until, 10000);
}

module.exports = wait;
