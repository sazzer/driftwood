// @flow

import React from 'react';
import {NamespacesConsumer} from 'react-i18next';
import {Dropdown} from 'semantic-ui-react';
import LoginMenuItem from './LoginMenuItem';

/**
 * The props for the Login Menu component
 */
export type LoginMenuProps = {
    providers: Array<string>
}

/**
 * The component representing the login menu
 * @constructor
 */
export function LoginMenu({t, providers}: LoginMenuProps) {
    const menuItems = providers.map(provider => <LoginMenuItem key={provider} provider={provider} />);

    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Dropdown item simple text={t('authentication.menu.title')} openOnFocus={false}>
                        <Dropdown.Menu>
                            { menuItems }
                        </Dropdown.Menu>
                    </Dropdown>
                )
            }
        </NamespacesConsumer>
    );
}


export default LoginMenu;
