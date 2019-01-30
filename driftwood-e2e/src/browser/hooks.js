const {AfterAll, BeforeAll, Before} = require('cucumber');
const {Builder} = require('selenium-webdriver');
const Browser = require('./Browser');

let driver;

BeforeAll(async () => {
    driver = new Builder()
        .forBrowser('chrome')
        .build();
    console.log('Created web browser');
});

Before(function() {
    this._browser = new Browser(driver);
});

AfterAll(async function() {
    await driver.quit();
    console.log('Quit web browser');
});
