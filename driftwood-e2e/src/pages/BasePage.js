const {By} = require('selenium-webdriver');
const PageHeader = require('./PageHeader');

class BasePage {
    /**
     * Construct the page
     * @param browser the browser
     */
    constructor(browser) {
        this._browser = browser;
    }

    async getHeader() {
        const pageRoot = await this._browser.getPageRoot();
        const headerElement = await pageRoot.findElement(By.css('[data-test="header"]'));
        return new PageHeader(headerElement);
    }
}

module.exports = BasePage;
