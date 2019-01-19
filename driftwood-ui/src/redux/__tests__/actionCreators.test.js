// @flow

import * as testSubject from '../actionCreators';

describe('buildActionName', () => {
    it('Builds a simple action name', () => {
        expect(testSubject.buildActionName('TEST')).toEqual('TEST');
    });
    it('Builds a namespaced action name', () => {
        expect(testSubject.buildActionName('TEST', 'NS')).toEqual('NS/TEST');
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
