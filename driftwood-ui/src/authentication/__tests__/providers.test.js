// @flow

import * as testSubject from '../providers';

describe('loadProviders()', () => {
    it('Returns the correct action', () => {
        const action = testSubject.loadProviders();

        expect(action).toEqual({
            type: 'AUTH/LOAD_PROVIDERS'
        });
    });
});

describe('selectProviders()', () => {
    it('Returns the providers from the state', () => {
        const providers = testSubject.selectProviders({
            providers: ['a', 'b']
        });

        expect(providers).toEqual(['a', 'b']);
    });
});

describe('loadProvidersReducer()', () => {
    it('Returns the correct new state', () => {
       const oldState = {providers: ['a']};
       const newState = testSubject.loadProvidersReducer(oldState);

       expect(newState).toEqual({providers: ['facebook']});
    });

    it('Doesn\'t mutate the old state', () => {
        const oldState = {providers: ['a']};
        const newState = testSubject.loadProvidersReducer(oldState);

        expect(oldState).toEqual({providers: ['a']});
    });
});
