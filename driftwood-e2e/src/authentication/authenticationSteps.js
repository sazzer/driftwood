const {Then} = require('cucumber');
const expect = require('unexpected');
const HomePage = require('../pages/HomePage');

Then('I am not logged in', async function () {
    const homePage = this._browser.getPageModel(HomePage);
    const header = await homePage.getHeader();
    await header.getLoginMenu();
});

Then('the authentication options are:', async function (options) {
    const homePage = this._browser.getPageModel(HomePage);
    const header = await homePage.getHeader();
    const loginMenu = await header.getLoginMenu();

    const providers = await loginMenu.getProviders();

    expect(providers, 'to equal', options.raw()[0]);
});
