// @flow

import {buildTest, flushAllPromises} from "./buildTest";
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

/** The Axios to Mock */
const axiosMock = new MockAdapter(axios);

describe('Login Menu', () => {
    it('Renders a pending Login menu on initial rendering', () => {
        axiosMock.onGet('/api/authentication/external')
            .reply(200, []);

        const {app} = buildTest();

        app.render();
        app.update();

        const loginMenu = app.find('div[data-test="loginMenu"]');
        expect(loginMenu).toHaveClassName('loading');
        expect(loginMenu).not.toHaveClassName('error');

        const menuItems = loginMenu.find('div[data-provider]');
        expect(menuItems).toHaveLength(0);

    });

    it('Renders a populated Login menu when the Ajax call to get the providers returns success', async () => {
        axiosMock.onGet('/api/authentication/external')
            .reply(200, ['google', 'twitter']);

        const {app} = buildTest();

        await flushAllPromises();

        app.render();
        app.update();

        const loginMenu = app.find('div[data-test="loginMenu"]');

        expect(loginMenu).not.toHaveClassName('loading');
        expect(loginMenu).not.toHaveClassName('error');

        const menuItems = loginMenu.find('div[data-provider]');
        expect(menuItems).toHaveLength(2);
        expect(menuItems.at(0)).toHaveProp('data-provider', 'google');
        expect(menuItems.at(1)).toHaveProp('data-provider', 'twitter');
    });

    it('Renders the Login menu as expected when the Ajax call to get the providers fails', async () => {
        axiosMock.onGet('/api/authentication/external')
            .timeout();

        const {app} = buildTest();

        await flushAllPromises();

        app.render();
        app.update();

        const loginMenu = app.find('div[data-test="loginMenu"]');

        expect(loginMenu).not.toHaveClassName('loading');
        expect(loginMenu).toHaveClassName('error');

        const menuItems = loginMenu.find('div[data-provider]');
        expect(menuItems).toHaveLength(0);
    });
});
