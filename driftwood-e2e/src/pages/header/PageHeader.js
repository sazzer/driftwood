const {By, until} = require('selenium-webdriver');
const wait = require('../../browser/wait');
const LoginMenu = require('./LoginMenu');
const ProfileMenu = require('./ProfileMenu');

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
        const locator = By.css('[data-test="loginMenu"]');

        await wait(until.elementLocated(locator));

        const loginMenuElement = await this._element.findElement(locator);
        return new LoginMenu(loginMenuElement);
    }

    /**
     * Get the profile menu
     * @return the profile menu
     */
    async getProfileMenu() {
        const locator = By.css('[data-test="profileMenu"]');

        await wait(until.elementLocated(locator));

        const profileMenuElement = await this._element.findElement(locator);
        return new ProfileMenu(profileMenuElement);
    }
}

module.exports = PageHeader;
