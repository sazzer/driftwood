// @flow

import React from 'react';
import {NamespacesConsumer} from 'react-i18next';
import {Menu} from 'semantic-ui-react';
import LoginMenu from './ConnectedLoginMenu';

/**
 * The component representing the header of the page
 * @constructor
 */
export function HeaderBar() {
    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Menu attached='top' inverted>
                        <Menu.Item header position='left'>{t('page.title')}</Menu.Item>
                        <Menu.Menu position='right'>
                            <LoginMenu />
                        </Menu.Menu>
                    </Menu>

                )
            }
        </NamespacesConsumer>
    );
}

export default HeaderBar;
