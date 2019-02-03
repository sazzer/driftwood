const {By, until} = require('selenium-webdriver');
const wait = require('../../browser/wait');

/**
 * Page Object representing the profile menu
 */
class ProfileMenu {
    /**
     * Construct the profile menu
     * @param element the web element representing the profile menu
     */
    constructor(element) {
        this._element = element;
    }

    /**
     * Check if the menu is open
     * @return True if the menu is open. False if not
     */
    async isOpen() {
        const expanded = await this._element.getAttribute('aria-expanded');
        return expanded === 'true';
    }

    /**
     * Open the menu
     */
    async open() {
        const isOpen = await this.isOpen();
        if (!isOpen) {
            await this._element.click();
        }
    }

    /**
     * Get the name of the user that is logged in
     * @return The name of the user
     */
    async getUserName() {
        const nameElement = await this._element.findElement(By.css('div.text[role="alert"]'));
        return await nameElement.getText();
    }

    /**
     * Log out of the application
     */
    async logout() {
        this.open();
        const logoutElement = await this._element.findElement(By.css('[data-test="logout"]'));
        await wait(until.elementIsVisible(logoutElement));

        await logoutElement.click();
    }
}

module.exports = ProfileMenu;
