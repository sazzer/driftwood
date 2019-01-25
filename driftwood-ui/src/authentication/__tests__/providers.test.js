// @flow

import * as testSubject from '../providers';

describe('Selectors', () => {
    describe('selectProviders()', () => {
        it('Returns the providers from the store', () => {
            const providers = testSubject.selectProviders({
                ...testSubject.initialState,
                providers: [
                    {
                        id: 'a',
                        uri: 'a',
                    }, {
                        id: 'b',
                        uri: 'b',
                    },
                ]
            });

            expect(providers).toEqual(['a', 'b']);
        });
    });

    describe('selectProviderById()', () => {
        const store = {
            ...testSubject.initialState,
            providers: [
                {
                    id: 'a',
                    uri: 'a',
                }, {
                    id: 'b',
                    uri: 'b',
                },
            ]
        };

        it('Returns a known provider from the store', () => {
            const provider = testSubject.selectProviderById(store, 'a');

            expect(provider).toEqual({id: 'a', uri: 'a'});
        });

        it('Returns undefined for an unknown provider', () => {
            const provider = testSubject.selectProviderById(store, 'unknown');

            expect(provider).toBeUndefined();
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
    const oldState = {
        ...testSubject.initialState,
        providers: [
            {
                id: 'a',
                uri: 'a',
            }, {
                id: 'b',
                uri: 'b',
            },
        ]
    };

    describe('storeProvidersStartedReducer()', () => {
        it('Returns the correct new state', () => {
            const newState = testSubject.storeProvidersStartedReducer(oldState);

            expect(newState).toEqual({providers: [
                    {
                        id: 'a',
                        uri: 'a',
                    },
                    {
                        id: 'b',
                        uri: 'b',
                    },
                ], state: 'loading'});
        });

        it('Doesn\'t mutate the old state', () => {
            const originalState = {...oldState};
            testSubject.storeProvidersStartedReducer(oldState);

            expect(oldState).toEqual(originalState);
        });
    });

    describe('storeProvidersSuccessReducer()', () => {
        const action = {type: '', payload: {result: [
                    {
                        id: 'facebook',
                        uri: 'facebookUri',
                    },
                ]}};
        it('Returns the correct new state', () => {
            const newState = testSubject.storeProvidersSuccessReducer(oldState, action);

            expect(newState).toEqual({providers: [
                    {
                        id: 'facebook',
                        uri: 'facebookUri',
                    }
                ], state: 'loaded'});
        });

        it('Doesn\'t mutate the old state', () => {
            const originalState = {...oldState};
            testSubject.storeProvidersSuccessReducer(oldState, action);

            expect(oldState).toEqual(originalState);
        });
    });

    describe('storeProvidersFailedReducer()', () => {
        it('Returns the correct new state', () => {
            const newState = testSubject.storeProvidersFailedReducer(oldState);

            expect(newState).toEqual({providers: [
                    {
                        id: 'a',
                        uri: 'a',
                    },
                    {
                        id: 'b',
                        uri: 'b',
                    },
                ], state: 'failed'});
        });

        it('Doesn\'t mutate the old state', () => {
            const originalState = {...oldState};
            testSubject.storeProvidersFailedReducer(oldState);

            expect(oldState).toEqual(originalState);

        });
    });
});

