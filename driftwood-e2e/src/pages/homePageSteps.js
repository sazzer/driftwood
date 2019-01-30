const {When} = require('cucumber');
const HomePage = require('./HomePage');

When('I load the home page', async function() {
    const homePage = this._browser.getPageModel(HomePage);
    await homePage.visit();
});
