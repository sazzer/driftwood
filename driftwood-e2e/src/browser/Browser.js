/**
 * Wrapper around the web browser
 */
class Browser {
    /**
     * Construct the web browser wrapper
     * @param driver the web driver
     */
    constructor(driver) {
        this._driver = driver;
    }

    /**
     * Visit the given URL
     * @param url the URL
     */
    async visit(url) {
        await this._driver.get(url);
    }

    /**
     * Get a page model for the given class
     * @param pageModel the page model class
     */
    getPageModel(pageModel) {
        return new pageModel(this);
    }
}

module.exports = Browser;
