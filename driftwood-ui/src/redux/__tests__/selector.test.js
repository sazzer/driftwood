// @flow

import { buildSelector } from '../selector';

it('Returns the correct data from a single nested scope', () => {
    const state = {
        a: {
            b: 1
        }
    };

    const selector = buildSelector(['a'], (state) => state.b);

    const result = selector(state);

    expect(result).toEqual(1);
});

it('Returns the correct data from a deeply nested scope', () => {
    const state = {
        a: {
            b: {
                c: 2
            }
        }
    };

    const selector = buildSelector(['a', 'b'], (state) => state.c);

    const result = selector(state);

    expect(result).toEqual(2);
});

it('Returns undefined if the nested scope doesn\'t exist', () => {
    const state = {
        a: {
            b: 1
        }
    };

    const selector = buildSelector(['unknown'], (state) => state.b);

    const result = selector(state);

    expect(result).toBeUndefined();
});


it('Passes arguments to the selector', () => {
    const state = {
        a: {
            b: 1
        }
    };

    const selector = buildSelector(['a'], (state, arg1) => state.b + arg1);

    const result = selector(state, 'test');

    expect(result).toEqual('1test');
});
