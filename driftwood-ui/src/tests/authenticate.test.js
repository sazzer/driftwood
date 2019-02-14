// @flow

import {buildTest, flushAllPromises} from "./buildTest";
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

/** The Axios to Mock */
const axiosMock = new MockAdapter(axios);

describe('Authentication', () => {
    let app;

    beforeAll(async () => {
        axiosMock.onGet('/api/authentication/external')
            .reply(200, ['google']);

        app = buildTest();

        await flushAllPromises();

        app.render();
        app.update();

        const loginMenu = app.find('div[data-test="loginMenu"]');

        window.open = jest.fn(() => {});

        let authenticateCallback = undefined;
        window.addEventListener = jest.fn((type, callback) => {
            if (type === 'message') {
                authenticateCallback = callback;
            }
        });

        window.removeEventListener = jest.fn();

        loginMenu.find('div[data-provider="google"]').simulate('click');

        expect(authenticateCallback).not.toBeUndefined();

        authenticateCallback({
            data: {
                type: 'driftwoodAccessToken',
                accessToken: 'accessToken',
                expires: 'expires',
                user: 'user',
                name: 'Users Name'
            }
        });

        await flushAllPromises();

        app.render();
        app.update();
    });

    it('Stops rendering the Login Menu when authenticated', async () => {
        const loginMenu = app.find('div[data-test="loginMenu"]');
        expect(loginMenu).not.toExist();
    });

    it('Renders the Profile Menu when authenticated', async () => {
        const profileMenu = app.find('div[data-test="profileMenu"]');
        expect(profileMenu).toExist();

        expect(profileMenu.find('div[role="alert"]')).toHaveText('Users Name');
    });
});
