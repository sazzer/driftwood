// @flow

import type {AccessToken} from "../accessToken";
import * as testSubject from '../accessToken';

const accessToken: AccessToken = {
    accessToken: 'accessToken',
    expires: 'expires',
    userId: 'userId',
};

describe('Selectors', () => {
    describe('selectProviders()', () => {
        it('Returns undefined if there is no token the store', () => {
            const token = testSubject.selectAccessToken({
                ...testSubject.initialState,
            });

            expect(token).toBeUndefined();
        });

        it('Returns the providers from the store', () => {
            const token = testSubject.selectAccessToken({
                ...testSubject.initialState,
                token: accessToken
            });

            expect(token).toEqual(accessToken);
        });
    });

});

describe('Store Access Token', () => {
    const oldState = {
        ...testSubject.initialState
    };

    describe('storeAccessTokenReducer()', () => {
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

