// @flow

import {buildTest, flushAllPromises} from "./buildTest";
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

/** The Axios to Mock */
const axiosMock = new MockAdapter(axios);

describe('Authentication', () => {
    let app;
    let store;

    beforeAll(async () => {
        axiosMock.onGet('/api/authentication/external')
            .reply(200, ['google']);

        const test = buildTest();
        app = test.app;
        store = test.store;

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

        if (!authenticateCallback) {
            // This can't happen because of the above, but is here to keep Flow happy
            return;
        }

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

    it('Updates the store correctly when authenticated', async () => {
        expect(store.getState()).toMatchObject({
            auth: {
                accessToken: {
                    token: {
                        accessToken: 'accessToken',
                        expires: 'expires',
                        userId: 'user'
                    }
                }
            },
            users: {
                userProfiles: {
                    users: {
                        user: {
                            profile: {
                                id: 'user',
                                name: 'Users Name'
                            },
                            status: 'LOADED'
                        }
                    }
                }
            }
        });
    });
});
