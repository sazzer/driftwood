// @flow

import React from 'react';
import {NamespacesConsumer} from 'react-i18next';
import {Dropdown} from 'semantic-ui-react';
import LoginMenuItem from './LoginMenuItem';

/** The possible statuses of the Login Menu */
export const LoginMenuStatus = {
    loading: Symbol('loading'),
    loaded: Symbol('loaded'),
    failed: Symbol('failed'),
};

/**
 * The props for the Login Menu component
 */
export type LoginMenuProps = {
    providers: Array<string>,
    status?: Symbol,
}

/**
 * The component representing the login menu
 * @constructor
 */
export function LoginMenu({providers, status}: LoginMenuProps) {
    let menuItems;

    if (status === LoginMenuStatus.failed) {
        menuItems = (
            <NamespacesConsumer>
                {
                    (t) => <Dropdown.Item icon='ban' text={t(`authentication.menu.loadingError`)} />
                }
            </NamespacesConsumer>
        );
    } else {
        menuItems = providers.map(provider => <LoginMenuItem key={provider} provider={provider} />);
    }

    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Dropdown item
                              simple
                              text={t('authentication.menu.title')}
                              openOnFocus={false}
                              data-test='loginMenu'
                              loading={status === LoginMenuStatus.loading}
                              error={status === LoginMenuStatus.failed} >
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
