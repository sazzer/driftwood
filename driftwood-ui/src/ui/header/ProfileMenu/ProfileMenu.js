// @flow

import React from 'react';
import {Dropdown} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";
import UsernameConsumer from "../../../users/UsernameConsumer";

/**
 * The props for the Profile Menu component
 */
export type ProfileMenuProps = {
    userId: string,
}

/**
 * The component representing the profile menu
 * @constructor
 */
export default function ProfileMenu({userId}: ProfileMenuProps) {
    return (
        <UsernameConsumer userId={userId}>
            {(username) =>
                <Dropdown item simple text={username} openOnFocus={false} data-test='profileMenu'>
                    <NamespacesConsumer>
                        {(t) =>
                            <Dropdown.Menu>
                                <Dropdown.Item data-test='profile' text={t('profile.menu.editProfile')} />
                                <Dropdown.Divider/>
                                <Dropdown.Item data-test='logout' text={t('profile.menu.logout')} />
                            </Dropdown.Menu>
                        }
                    </NamespacesConsumer>
                </Dropdown>
            }
        </UsernameConsumer>
    );
}
