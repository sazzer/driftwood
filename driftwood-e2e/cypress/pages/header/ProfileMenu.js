/// <reference types="cypress" />

export default class ProfileMenu {
    constructor(element) {
        this._element = element;
        element.should('not.have.class', 'loading');
    }

    assertUsersName(username) {
        this._element.get('div[data-test="profileMenu"] div[role="alert"]').invoke('text').should('equal', username);
    }
}
