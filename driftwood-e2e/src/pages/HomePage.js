/**
 * Page Model for the HomePage
 */
class HomePage {
    /**
     * Construct the home page
     * @param browser the browser
     */
    constructor(browser) {
        this._browser = browser;
    }

    /**
     * Open the home page
     */
    async visit() {
        this._browser.visit('http://www.google.com');
    }
}
