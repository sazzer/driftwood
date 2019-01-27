// @flow

import type {AccessToken} from "../accessToken";
import * as testSubject from '../accessToken';

describe('Selectors', () => {
});

describe('Store Access Token', () => {
    const oldState = {
        ...testSubject.initialState
    };

    describe('storeAccessTokenReducer()', () => {
        const accessToken: AccessToken = {
            accessToken: 'accessToken',
            expires: 'expires',
            userId: 'userId',
        };

        it('Returns the correct new state', () => {
            const newState = testSubject.storeAccessTokenReducer(oldState, {
                type: '',
                payload: accessToken
            });

            expect(newState).toEqual({token: accessToken});
        });

        it('Doesn\'t mutate the old state', () => {
            const originalState = {...oldState};
            testSubject.storeAccessTokenReducer(oldState, {
                type: '',
                payload: accessToken
            });

            expect(oldState).toEqual(originalState);
        });
    });

});

