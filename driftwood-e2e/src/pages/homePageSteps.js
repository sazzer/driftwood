const {When, Then} = require('cucumber');
const expect = require('unexpected');
const HomePage = require('./HomePage');

When('I load the home page', async function() {
    const homePage = this._browser.getPageModel(HomePage);
    await homePage.visit();
});

Then('the page title should be {string}', async function (title) {
    const homePage = this._browser.getPageModel(HomePage);
    const header = await homePage.getHeader();
    const pageTitle = await header.getTitle();
    expect(pageTitle, 'to equal', title);
});
