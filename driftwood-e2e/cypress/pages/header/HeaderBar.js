/// <reference types="cypress" />

import LoginMenu from "./LoginMenu";

export default class HeaderBar {
    constructor(element) {
        this._element = element;
    }

    get title() {
        return this._element.get('div[data-test="title"]').invoke('text');
    }

    get loginMenu() {
        return new LoginMenu(this._element.get('div[data-test="loginMenu"]'));
    }
}
