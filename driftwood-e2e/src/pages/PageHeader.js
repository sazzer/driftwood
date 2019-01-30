const {By} = require('selenium-webdriver');

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
}

module.exports = PageHeader;
