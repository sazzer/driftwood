const BasePage = require('./BasePage');

/**
 * Page Model for the User Profile
 */
class UserProfilePage extends BasePage {
    /**
     * Construct the home page
     * @param browser the browser
     */
    constructor(browser) {
        super(browser);
    }

    /**
     * Open the home page
     */
    async visit() {
        await this._browser.visit('/profile');
    }
}

module.exports = UserProfilePage;
