const {When} = require('cucumber');
const UserProfilePage = require('./pages/UserProfilePage');

When('I load the user profile page', {timeout: 60 * 1000}, async function() {
    const homePage = this._browser.getPageModel(UserProfilePage);
    await homePage.visit();
});
