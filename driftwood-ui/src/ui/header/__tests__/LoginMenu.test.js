// @flow

import React from 'react';
import {render} from 'enzyme';
import LoginMenu from '../LoginMenu';
import i18n from "../../../i18n";
import {I18nextProvider} from "react-i18next";

/** Set up the component to test */
function setup(providers: Array<string>) {
    return {
        element: render(<I18nextProvider i18n={ i18n }><LoginMenu providers={providers} /></I18nextProvider>)
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
