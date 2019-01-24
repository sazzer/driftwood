/// <reference types="cypress" />

export default class LoginMenu {
    constructor(element) {
        this._element = element;
        element.should('not.have.class', 'loading');
    }

    assertIsOpen() {
        this._element.should('have.class', 'visible');
    }

    assertIsClosed() {
        this._element.should('not.have.class', 'visible');
    }

    assertProviderPresent(provider) {
        this._element.get(`div.menu div[data-provider="${provider}"]`);
    }

    open() {
        this._element.click();
    }
}
