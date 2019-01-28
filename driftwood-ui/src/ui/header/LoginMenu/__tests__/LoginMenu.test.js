// @flow

import React from 'react';
import {render, mount} from 'enzyme';
import LoginMenu from '../LoginMenu';
import i18n from "../../../../i18n";
import {I18nextProvider} from "react-i18next";

/** Set up the component to test */
function setup(providers: Array<string>, fullMount: boolean = false) {
    const authenticate = jest.fn();

    const renderFn = fullMount ? mount : render;

    return {
        element: renderFn(<I18nextProvider i18n={ i18n }><LoginMenu providers={providers} authenticate={authenticate} /></I18nextProvider>),
        authenticate,
    };
}

it('Renders correctly with no providers', () => {
    const { element } = setup([]);

    expect(element).toMatchSnapshot();
});

it('Renders correctly with some providers', () => {
    const { element } = setup(['google', 'twitter']);

    expect(element).toMatchSnapshot();
});

it('Responds correctly when an item is clicked', () => {
    const { element, authenticate } = setup(['google', 'twitter'], true);

    expect(authenticate.mock.calls.length).toBe(0);

    element.find('LoginMenuItem[provider="google"]').simulate('click');

    expect(authenticate.mock.calls.length).toBe(1);
    expect(authenticate.mock.calls[0][0]).toBe('google');
});
