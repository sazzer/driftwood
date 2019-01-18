// @flow

import React from 'react';
import {render} from 'enzyme';
import LoginMenuItem from '../LoginMenuItem';
import i18n from "../../../i18n";
import {I18nextProvider} from "react-i18next";

/** Set up the component to test */
function setup(provider: string) {
    return {
        element: render(<I18nextProvider i18n={ i18n }><LoginMenuItem provider={provider} /></I18nextProvider>)
    };
}

it('Renders correctly', () => {
    const { element } = setup('google');

    expect(element).toMatchSnapshot();
});
