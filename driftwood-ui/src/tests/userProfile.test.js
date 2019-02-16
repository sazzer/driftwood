// @flow

import {buildTest, flushAllPromises} from "./buildTest";
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
        axiosMock.onGet('/api/users/' + USER_ID)
            .reply(200, {});

        test.authenticate({userId: USER_ID});

        await flushAllPromises();

        test.app.render();
        test.app.update();

        expect(test.app.find('NotAuthenticated')).not.toExist();
    });
});
