// @flow

import * as testSubject from '../actionCreators';

describe('buildActionName', () => {
    it('Builds a simple action name', () => {
        expect(testSubject.buildActionName('TEST')).toEqual('TEST');
    });
    it('Builds a namespaced action name', () => {
        expect(testSubject.buildActionName('TEST', 'NS')).toEqual('NS/TEST');
    });
    it('Builds an action with a suffix', () => {
        expect(testSubject.buildActionName('TEST', undefined, 'SUCCESS')).toEqual('TEST/SUCCESS');
    });
    it('Builds an action with namespace and suffix', () => {
        expect(testSubject.buildActionName('TEST', 'NS', 'SUCCESS')).toEqual('NS/TEST/SUCCESS');
    });
});

describe('createAction', () => {
    it('Creates an action creator with no payload', () => {
        const actionCreator = testSubject.createAction('TEST');
        const action = actionCreator();

        expect(action).toEqual({
            type: 'TEST'
        });
    });

    it('Creates an action creator with payload', () => {
        const actionCreator = testSubject.createAction('TEST');
        const action = actionCreator('payload');

        expect(action).toEqual({
            type: 'TEST',
            payload: 'payload'
        });
    });
});
