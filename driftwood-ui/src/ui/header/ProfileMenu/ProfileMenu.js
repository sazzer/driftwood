// @flow

import React from 'react';
import {Dropdown} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";

/**
 * The props for the Profile Menu component
 */
export type ProfileMenuProps = {
}

/**
 * The component representing the profile menu
 * @constructor
 */
export function ProfileMenu({}: ProfileMenuProps) {
    return (
        <NamespacesConsumer>
            {
                (t) => <Dropdown item
                                 simple
                                 text='Graham'
                                 openOnFocus={false}
                                 data-test='profileMenu'>
                    <Dropdown.Menu>
                        <Dropdown.Item data-test='profile'
                                       text={t('profile.menu.editProfile')} />
                        <Dropdown.Divider/>
                        <Dropdown.Item data-test='logout'
                                       text={t('profile.menu.logout')} />
                    </Dropdown.Menu>
                </Dropdown>
            }
        </NamespacesConsumer>
    );
}

export default ProfileMenu;
