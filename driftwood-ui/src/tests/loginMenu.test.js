// @flow

import {buildTest, flushAllPromises} from "./buildTest";
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

/** The Axios to Mock */
const axiosMock = new MockAdapter(axios);

it('Renders a pending Login menu on initial rendering', () => {
    axiosMock.onGet('/api/authentication/external')
        .reply(200, []);

    const app = buildTest();

    app.render();
    app.update();

    const loginMenu = app.find('div[data-test="loginMenu"]');
    expect(loginMenu).toHaveClassName('loading');

});

it('Renders a populated Login menu when the Ajax call to get the providers returns success', async () => {
    axiosMock.onGet('/api/authentication/external')
        .reply(200, ['google', 'twitter']);

    const app = buildTest();

    await flushAllPromises();

    app.render();
    app.update();

    const loginMenu = app.find('div[data-test="loginMenu"]');

    expect(loginMenu).not.toHaveClassName('loading');

    const menuItems = loginMenu.find('div[data-provider]');
    expect(menuItems.at(0)).toHaveProp('data-provider', 'google');
    expect(menuItems.at(1)).toHaveProp('data-provider', 'twitter');
});
