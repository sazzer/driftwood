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
    console.log('Created web browser');
});

Before(function() {
    this._browser = new Browser(driver);
});

After(async function(testCase) {
    const fileBase = 'screenshots/out';

    const image = await driver.takeScreenshot();
    fs.writeFileSync(`${fileBase}.png`, image, 'base64');

    const logs = await driver.manage().logs().get(logging.Type.BROWSER);
    const browserLog = logs.map(entry => `[${entry.level.name}] ${entry.message}`)
        .join('\n');
    fs.writeFileSync(`${fileBase}.log`, browserLog);
});

AfterAll(async function() {
    await driver.quit();
    console.log('Quit web browser');
});
