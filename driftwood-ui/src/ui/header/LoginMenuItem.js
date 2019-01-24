// @flow

import React from 'react';
import {NamespacesConsumer} from 'react-i18next';
import {Dropdown} from 'semantic-ui-react';

/**
 * The props for the Login Menu Item component
 */
type LoginMenuItemProps = {
    provider: string
}

/**
 * The component representing a single item on the login menu
 * @constructor
 */
export function LoginMenuItem({provider}: LoginMenuItemProps) {
    return (
        <NamespacesConsumer>
            {
                (t) => <Dropdown.Item icon={provider} data-provider={provider} text={t(`authentication.menu.${provider}`)} />
            }
        </NamespacesConsumer>

    );
}

export default LoginMenuItem;
