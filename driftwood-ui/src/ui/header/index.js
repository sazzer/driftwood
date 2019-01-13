// @flow

import React from 'react';
import {withI18n} from 'react-i18next';
import {Menu} from 'semantic-ui-react';
import LoginMenu from './LoginMenu';

type HeaderBarProps = {
    t: (string) => string
}

/**
 * The component representing the header of the page
 * @constructor
 */
export function HeaderBar({t}: HeaderBarProps) {
    return (
        <Menu attached='top' inverted>
            <Menu.Item header position='left'>{t('page.title')}</Menu.Item>
            <Menu.Menu position='right'>
                <LoginMenu />
            </Menu.Menu>
        </Menu>
    );
}

export default withI18n()(HeaderBar);
