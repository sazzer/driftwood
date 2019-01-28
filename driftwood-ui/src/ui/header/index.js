// @flow

import React from 'react';
import {NamespacesConsumer} from 'react-i18next';
import {Menu} from 'semantic-ui-react';
import UserMenu from './UserMenu';

/**
 * The component representing the header of the page
 * @constructor
 */
export function HeaderBar() {
    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Menu attached='top' inverted data-test='header'>
                        <Menu.Item header position='left' data-test='title'>{t('page.title')}</Menu.Item>
                        <Menu.Menu position='right'>
                            <UserMenu />
                        </Menu.Menu>
                    </Menu>

                )
            }
        </NamespacesConsumer>
    );
}

export default HeaderBar;
