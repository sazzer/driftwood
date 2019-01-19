// @flow

import * as testSubject from '../async';

describe('Action Names', () => {
    it('Creates the action name for starting an action', () => {
        expect(testSubject.startedAction('TEST')).toEqual('TEST_STARTED');
    }) ;
    it('Creates the action name for finishing an action', () => {
        expect(testSubject.finishedAction('TEST')).toEqual('TEST_FINISHED');
    }) ;
    it('Creates the action name for an action succeeding', () => {
        expect(testSubject.succeededAction('TEST')).toEqual('TEST_SUCCEEDED');
    }) ;
    it('Creates the action name for an action failing', () => {
        expect(testSubject.failedAction('TEST')).toEqual('TEST_FAILED');
    }) ;
});
