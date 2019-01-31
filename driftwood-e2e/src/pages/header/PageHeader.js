const {By} = require('selenium-webdriver');
const LoginMenu = require('./LoginMenu');

/**
 * Page Object representing the page header
 */
class PageHeader {
    /**
     * Construct the page header
     * @param element the web element representing the header
     */
    constructor(element) {
        this._element = element;
    }

    /**
     * Get the title of the page
     * @return the page title
     */
    async getTitle() {
        const titleElement = await this._element.findElement(By.css('[data-test="title"]'));
        return await titleElement.getText();
    }

    /**
     * Get the login menu
     * @return the login menu
     */
    async getLoginMenu() {
        const loginMenuElement = await this._element.findElement(By.css('[data-test="loginMenu"]'));
        return new LoginMenu(loginMenuElement);
    }
}

module.exports = PageHeader;
