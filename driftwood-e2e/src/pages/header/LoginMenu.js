const {By} = require('selenium-webdriver');

/**
 * Page Object representing the login menu
 */
class LoginMenu {
    /**
     * Construct the login menu
     * @param element the web element representing the login menu
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
     * Get the providers with which we can log in
     * @return The list of providers
     */
    async getProviders() {
        await this.open();

        const providerOptions = await this._element.findElements(By.css('[data-provider]'));
        const providers = providerOptions.map(async (provider) => await provider.getAttribute('data-provider'));

        return await Promise.all(providers);
    }
}

module.exports = LoginMenu;
