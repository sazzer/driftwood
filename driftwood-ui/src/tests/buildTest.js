// @flow

import React from 'react';
import {I18nextProvider, translate} from "react-i18next";
import {mount} from 'enzyme';
import {Provider} from "react-redux";
import {ConnectedRouter as Router} from "connected-react-router";
import {history, buildStore} from "../redux";
import i18n from '../i18n';
import App from "../ui/App";

/** Mechanism to flush outstanding promises */
export const flushAllPromises = async () => {
    await new Promise(resolve => setImmediate(resolve));
};

/**
 * Build the application to test
 * @return the application to test
 */
export function buildTest() {
    const TranslatedAppContents = translate(['driftwood'], {wait: true})(App);

    const store = buildStore();
    const app = mount(
        <I18nextProvider i18n={ i18n }>
            <Provider store={store}>
                <Router history={history}>
                    <TranslatedAppContents />
                </Router>
            </Provider>
        </I18nextProvider>
    );

    return {app, store};
}
