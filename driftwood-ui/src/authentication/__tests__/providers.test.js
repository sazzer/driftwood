// @flow

import * as testSubject from '../providers';

describe('Selectors', () => {
    describe('selectProviders()', () => {
        it('Returns the providers from the store', () => {
            const providers = testSubject.selectProviders({
                ...testSubject.initialState,
                providers: ['a', 'b']
            });

            expect(providers).toEqual(['a', 'b']);
        });
    });

    describe('selectProviderLoadState()', () => {
        it('Returns the load state from the store', () => {
            const loadState = testSubject.selectProviderLoadState({
                ...testSubject.initialState,
                state: 'loading',
            });

            expect(loadState).toEqual('loading');
        });
    });
});

describe('Load Providers', () => {
    describe('loadProviders()', () => {
        it('Returns the correct action', () => {
            const action = testSubject.loadProviders();

            expect(action).toEqual({
                type: 'AUTH/PROVIDERS/LOAD_PROVIDERS'
            });
        });
    });
});

describe('Store Providers', () => {
    describe('storeProvidersStartedReducer()', () => {
        it('Returns the correct new state', () => {
            const oldState = {
                ...testSubject.initialState,
                providers: ['a']
            };
            const newState = testSubject.storeProvidersStartedReducer(oldState);

            expect(newState).toEqual({providers: ['a'], state: 'loading'});
        });

        it('Doesn\'t mutate the old state', () => {
            const oldState = {providers: ['a']};
            testSubject.storeProvidersStartedReducer(oldState);

            expect(oldState).toEqual({providers: ['a']});
        });
    });

    describe('storeProvidersSuccessReducer()', () => {
        it('Returns the correct new state', () => {
            const oldState = {
                ...testSubject.initialState,
                providers: ['a']
            };
            const newState = testSubject.storeProvidersSuccessReducer(oldState, {type: '', payload: {result: ['facebook']}});

            expect(newState).toEqual({providers: ['facebook'], state: 'loaded'});
        });

        it('Doesn\'t mutate the old state', () => {
            const oldState = {providers: ['a']};
            testSubject.storeProvidersSuccessReducer(oldState, {type: '', payload: {result: ['facebook']}});

            expect(oldState).toEqual({providers: ['a']});
        });
    });

    describe('storeProvidersFailedReducer()', () => {
        it('Returns the correct new state', () => {
            const oldState = {
                ...testSubject.initialState,
                providers: ['a']
            };
            const newState = testSubject.storeProvidersFailedReducer(oldState);

            expect(newState).toEqual({providers: ['a'], state: 'failed'});
        });

        it('Doesn\'t mutate the old state', () => {
            const oldState = {providers: ['a']};
            testSubject.storeProvidersFailedReducer(oldState);

            expect(oldState).toEqual({providers: ['a']});
        });
    });
});

