// @flow

import {put, apply} from 'redux-saga/effects';
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

describe('Async Action', () => {
    describe('Yields that the action has started', () => {
        it('Includes the payload if there was one', () => {
            const gen = testSubject.asyncAction('TEST', () => {}, 'payload');
            const yieldValue = gen.next();
            expect(yieldValue.done).toEqual(false);
            expect(yieldValue.value).toEqual(put({type: 'TEST_STARTED', input: ['payload']}));
        });
        it('Doesn\'t include a payload if there wasn\'t one', () => {
            const gen = testSubject.asyncAction('TEST', () => {});
            const yieldValue = gen.next();
            expect(yieldValue.done).toEqual(false);
            expect(yieldValue.value).toEqual(put({type: 'TEST_STARTED', input: []}));
        });
    });

    describe('Calls the action', () => {
        const action = () => 1;

        it('Includes the payload if there was one', () => {
            const gen = testSubject.asyncAction('TEST', action, 'payload');
            gen.next(); // Action Started
            const yieldValue = gen.next();
            expect(yieldValue.done).toEqual(false);
            expect(yieldValue.value).toEqual(apply(null, action, ['payload']));
        });
        it('Doesn\'t include a payload if there wasn\'t one', () => {
            const gen = testSubject.asyncAction('TEST', action);
            gen.next(); // Action Started
            const yieldValue = gen.next();
            expect(yieldValue.done).toEqual(false);
            expect(yieldValue.value).toEqual(apply(null, action, []));
        });
    });

    describe('Action succeeded', () => {
        const action = () => 1;

        describe('Yields that the action was successful', () => {
            it('Includes the payload if there was one',async () => {
                const gen = testSubject.asyncAction('TEST', action, 'payload');
                gen.next(); // Action Started
                gen.next(); // Call Function
                const yieldValue = gen.next(1);
                expect(yieldValue.done).toEqual(false);
                await expect(yieldValue.value).toEqual(put({type: 'TEST_SUCCEEDED', input: ['payload'], payload: {result: 1}}));
            });
            it('Doesn\'t include a payload if there wasn\'t one', async () => {
                const gen = testSubject.asyncAction('TEST', action);
                gen.next(); // Action Started
                gen.next(); // Call Function
                const yieldValue = gen.next(1);
                expect(yieldValue.done).toEqual(false);
                await expect(yieldValue.value).toEqual(put({type: 'TEST_SUCCEEDED', input: [], payload: {result: 1}}));
            });
        });

        describe('Yields that the action has finished', () => {
            it('Includes the payload if there was one', () => {
                const gen = testSubject.asyncAction('TEST', action, 'payload');
                gen.next(); // Action Started
                gen.next(); // Call Function
                gen.next(); // Call Succeeded
                const yieldValue = gen.next();
                expect(yieldValue.done).toEqual(false);
                expect(yieldValue.value).toEqual(put({type: 'TEST_FINISHED', input: ['payload'], payload: {}}));
            });
            it('Doesn\'t include a payload if there wasn\'t one', () => {
                const gen = testSubject.asyncAction('TEST', action);
                gen.next(); // Action Started
                gen.next(); // Call Function
                gen.next(); // Call Succeeded
                const yieldValue = gen.next();
                expect(yieldValue.done).toEqual(false);
                expect(yieldValue.value).toEqual(put({type: 'TEST_FINISHED', input: [], payload: {}}));
            });
        });

        it('Doesn\'t yield anything else', () => {
            const gen = testSubject.asyncAction('TEST', action);
            gen.next(); // Action Started
            gen.next(); // Call Function
            gen.next(); // Call Succeeded
            gen.next(); // Action Finished
            const yieldValue = gen.next();
            expect(yieldValue.done).toEqual(true);
        });
    });
});
