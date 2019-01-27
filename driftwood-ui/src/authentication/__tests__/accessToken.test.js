// @flow

import type {AccessToken} from "../accessToken";
import * as testSubject from '../accessToken';

const accessToken: AccessToken = {
    accessToken: 'accessToken',
    expires: 'expires',
    userId: 'userId',
};

describe('Selectors', () => {
    describe('selectAccessToken()', () => {
        it('Returns undefined if there is no token the store', () => {
            const token = testSubject.selectAccessToken({
                ...testSubject.initialState,
            });

            expect(token).toBeUndefined();
        });

        it('Returns the access token from the store', () => {
            const token = testSubject.selectAccessToken({
                ...testSubject.initialState,
                token: accessToken
            });

            expect(token).toEqual(accessToken);
        });
    });

    describe('selectCurrentUser()', () => {
        it('Returns undefined if there is no token the store', () => {
            const user = testSubject.selectCurrentUser({
                ...testSubject.initialState,
            });

            expect(user).toBeUndefined();
        });

        it('Returns the current user from the store', () => {
            const user = testSubject.selectCurrentUser({
                ...testSubject.initialState,
                token: accessToken
            });

            expect(user).toEqual('userId');
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

