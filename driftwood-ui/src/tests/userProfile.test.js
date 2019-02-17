// @flow

import {buildTest, flushAllPromises} from './buildTest';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

/** The User ID to use */
const USER_ID = 'theUserId';

/** The Axios to Mock */
const axiosMock = new MockAdapter(axios);

describe('User Profile', () => {
    let test;

    beforeEach(() => {
        axiosMock.onGet('/api/authentication/external')
            .reply(200, []);

        axiosMock.onGet('/api/users/' + USER_ID)
            .reply(200, {
                'id': USER_ID,
                'name': 'Test User',
                'email': 'test@example.com',
                'logins': [{
                    'provider': 'google',
                    'providerId': 'googleUserId',
                    'displayName': 'test@example.com'
                }, {
                    'provider': 'twitter',
                    'providerId': 'twitterUserId',
                    'displayName': '@testuser'
                }]
            });

        test = buildTest();

        test.routeTo('/profile');
    });

    it('Renders a warning when not authenticated', async () => {
        await flushAllPromises();

        test.app.render();
        test.app.update();

        expect(test.app.find('NotAuthenticated')).toExist();
    });

    it('Does not render a warning when not authenticated', async () => {
        test.authenticate({userId: USER_ID});

        await flushAllPromises();

        test.app.render();
        test.app.update();

        expect(test.app.find('NotAuthenticated')).not.toExist();
    });

    it('Renders the correct user details from the server', async () => {
        test.authenticate({userId: USER_ID});

        await flushAllPromises();

        test.app.render();
        test.app.update();

        const profilePage = test.app.find('ProfilePage');
        expect(profilePage).toExist();

        // Form State
        const form = profilePage.find('Form');
        expect(form).toHaveProp('loading', false);
        expect(form).toHaveProp('error', false);
        expect(form.find('button.primary')).toHaveProp('disabled', true);
        expect(form.find('button.negative')).toHaveProp('disabled', true);

        // Breadcrumbs
        expect(profilePage.find('BreadcrumbSection[active] a')).toHaveText('Test User');

        // User name
        expect(profilePage.find('UserNameSection')).toHaveText('Test User');

        // Details Form
        const detailsForm = profilePage.find('AccountDetailsSection');
        expect(detailsForm.find('input[name="name"]')).toHaveValue('Test User');
        expect(detailsForm.find('input[name="email"]')).toHaveValue('test@example.com');

        // Providers List
        const providersTable = profilePage.find('LoginProvidersSection');
        const rows = providersTable.find('tr');
        expect(rows.at(0).find('td').at(1)).toHaveText('test@example.com');
        expect(rows.at(1).find('td').at(1)).toHaveText('@testuser');
    });
});
