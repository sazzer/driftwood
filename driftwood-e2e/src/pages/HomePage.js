const BasePage = require('./BasePage');

/**
 * Page Model for the HomePage
 */
class HomePage extends BasePage {
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
        this._browser.visit('/');
    }
}

module.exports = HomePage;
