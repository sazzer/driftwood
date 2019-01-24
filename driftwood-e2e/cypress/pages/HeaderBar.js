/// <reference types="cypress" />

export default class HeaderBar {
    constructor(headerElement) {
        this._headerElement = headerElement;
    }

    get title() {
        return this._headerElement.get('div.left.header.item').invoke('text');
    }
}
