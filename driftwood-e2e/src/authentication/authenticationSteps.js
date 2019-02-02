const {Then, When} = require('cucumber');
const expect = require('unexpected');
const HomePage = require('../pages/HomePage');

When('I authenticate with {string}', async function (provider) {
    const homePage = this._browser.getPageModel(HomePage);
    const header = await homePage.getHeader();
    const loginMenu = await header.getLoginMenu();

    await loginMenu.login(provider);

    profileMenu = await header.getProfileMenu();
});

When('I log out', async function () {
    const homePage = this._browser.getPageModel(HomePage);
    const header = await homePage.getHeader();
    const profileMenu = await header.getProfileMenu();

    await profileMenu.logout();
});

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

Then('I am logged in as {string}', async function (expectedUsername) {
    const homePage = this._browser.getPageModel(HomePage);
    const header = await homePage.getHeader();
    const profileMenu = await header.getProfileMenu();

    const userName = await profileMenu.getUserName();

    expect(userName, 'to equal', expectedUsername);
});
