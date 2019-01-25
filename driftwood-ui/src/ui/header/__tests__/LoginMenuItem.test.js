// @flow

import React from 'react';
import {render, mount} from 'enzyme';
import LoginMenuItem from '../LoginMenuItem';
import i18n from "../../../i18n";
import {I18nextProvider} from "react-i18next";

/** Set up the component to test */
function setup(provider: string, fullMount: boolean = false) {
    const onClick = jest.fn();

    const renderFn = fullMount ? mount : render;

    return {
        element: renderFn(<I18nextProvider i18n={ i18n }><LoginMenuItem provider={provider} onClick={onClick} /></I18nextProvider>),
        onClick,
    };
}

it('Renders correctly', () => {
    const { element } = setup('google');

    expect(element).toMatchSnapshot();
});


it('Reacts when clicked on', () => {
    const { element, onClick } = setup('google', true);

    expect(onClick.mock.calls.length).toBe(0);

    element.find('LoginMenuItem').simulate('click');

    expect(onClick.mock.calls.length).toBe(1);
});
