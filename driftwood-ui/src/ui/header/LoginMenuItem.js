// @flow

import React from 'react';
import {NamespacesConsumer} from 'react-i18next';
import {Dropdown} from 'semantic-ui-react';

/**
 * The props for the Login Menu Item component
 */
type LoginMenuItemProps = {
    provider: string,

    onClick: () => void,
}

/**
 * The component representing a single item on the login menu
 * @constructor
 */
export function LoginMenuItem({provider, onClick}: LoginMenuItemProps) {
    return (
        <NamespacesConsumer>
            {
                (t) => <Dropdown.Item icon={provider}
                                      data-provider={provider}
                                      text={t(`authentication.menu.${provider}`)}
                                      onClick={onClick} />
            }
        </NamespacesConsumer>

    );
}

export default LoginMenuItem;
