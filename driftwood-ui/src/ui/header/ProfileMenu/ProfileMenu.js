// @flow

import React from 'react';
import {Dropdown} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";
import UsernameConsumer from "../../../users/UsernameConsumer";
import {Link} from "react-router-dom";

/** Styles to apply to the Profile Link */
const ProfileLinkStyles = {
    color: 'black',
};

/**
 * The props for the Profile Menu component
 */
export type ProfileMenuProps = {
    userId: string,

    logout: () => void,
}

/**
 * The component representing the profile menu
 * @constructor
 */
export default function ProfileMenu({userId, logout}: ProfileMenuProps) {
    return (
        <UsernameConsumer userId={userId}>
            {(username) =>
                <Dropdown item simple text={username} openOnFocus={false} data-test='profileMenu'>
                    <NamespacesConsumer>
                        {(t) =>
                            <Dropdown.Menu>
                                <Dropdown.Item data-test='profile'>
                                    <Link to='/profile' style={ProfileLinkStyles}>
                                        {t('profile.menu.editProfile')}
                                    </Link>
                                </Dropdown.Item>

                                <Dropdown.Divider/>
                                <Dropdown.Item data-test='logout' text={t('profile.menu.logout')} onClick={logout} />
                            </Dropdown.Menu>
                        }
                    </NamespacesConsumer>
                </Dropdown>
            }
        </UsernameConsumer>
    );
}
