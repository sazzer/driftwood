// @flow

import React from 'react';
import {withI18n} from 'react-i18next';
import {Dropdown} from 'semantic-ui-react';

/**
 * The props for the Login Menu Item component
 */
type LoginMenuItemProps = {
    provider: string,
    t: (string) => string
}

/**
 * The component representing a single item on the login menu
 * @constructor
 */
export function LoginMenuItem({provider, t}: LoginMenuItemProps) {
    return (
        <Dropdown.Item icon={provider} text={t(`authentication.menu.${provider}`)} />
    );
}

export default withI18n()(LoginMenuItem);
