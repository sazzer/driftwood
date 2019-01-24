/// <reference types="cypress" />

/**
 * Page Model representing the application homepage
 */
import HeaderBar from "./HeaderBar";

export default class HomePage {
    visit() {
        cy.visit('/');
        return this;
    }

    get header() {
        const headerElement = cy.get('div[data-test="header"]');
        return new HeaderBar(headerElement);
    }
}
