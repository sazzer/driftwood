// @flow

import React from 'react';
import {I18nextProvider, translate} from "react-i18next";
import {mount} from 'enzyme';
import {Provider} from "react-redux";
import {ConnectedRouter as Router} from "connected-react-router";
import {history, buildStore} from "../redux";
import i18n from '../i18n';
import App from "../ui/App";
import uuid from 'uuid/v4';

/** Type representing the Redux store */
type Store = {
    dispatch: (any) => void,
    getState: () => { [string] : any},
};

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

    return {
        app,
        store,
        routeTo: (pathname: string, search?: string, hash?: string) => routeTo(store, pathname, search, hash),
        authenticate: (authenticationDetails?: AuthenticationDetails) => authenticate(store, authenticationDetails),
    };
}

/**
 * Send the action to route the application to a given location
 * @param store the store to send the action to
 * @param pathname the pathname to route to
 * @param search the querystring to route to
 * @param hash the hash to route to
 */
export function routeTo(store: Store, pathname: string, search?: string, hash?: string) {
    store.dispatch({
        type: '@@router/LOCATION_CHANGE',
        payload: {
            location: {
                pathname,
                search,
                hash,
            },
            action: 'PUSH',
            isFirstRendering: false
        }
    });
}

/** The shape of the authentication details to provide */
export type AuthenticationDetails = {
    userId?: string,
    accessToken?: string,
    name?: string,
    expires?: string,
}

/**
 * Send the action to store authentication details
 * @param store the store to send the action to
 */
export function authenticate(store: Store, {userId, accessToken, name, expires}: AuthenticationDetails = {}) {
    store.dispatch({
        type: 'AUTH/REMEMBER_LOGIN/STORE',
        payload: {
            accessToken: accessToken || uuid(),
            expires: expires || new Date(new Date().getTime() + (60 * 60 * 1000)).toISOString(),
            user: userId || uuid(),
            name: name || 'Test User',
        }
    });
}
