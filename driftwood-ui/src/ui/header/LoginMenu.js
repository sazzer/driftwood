// @flow

import React from 'react';
import {withI18n} from 'react-i18next';
import {Dropdown} from 'semantic-ui-react';
import LoginMenuItem from './LoginMenuItem';

/**
 * The props for the Login Menu component
 */
type LoginMenuProps = {
    t: (string) => string
}

/**
 * The component representing the login menu
 * @constructor
 */
export function LoginMenu({t}: LoginMenuProps) {
    const providers = ['facebook', 'google', 'twitter'];

    const menuItems = providers.map(provider => <LoginMenuItem provider={provider} />);

    return (
        <Dropdown item simple text={t('authentication.menu.title')} openOnFocus={false}>
            <Dropdown.Menu>
                { menuItems }
            </Dropdown.Menu>
        </Dropdown>
    );
}

export default withI18n()(LoginMenu);
