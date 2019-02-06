// @flow

import React from 'react';
import {Header} from 'semantic-ui-react';
import type {UserProfile} from "../../users/userProfiles";

/** Props for the Username Section */
type UserNameSectionProps = {
    values: UserProfile,
}

/**
 * Render the users name
 */
export default function UserNameSection({values} : UserNameSectionProps) {
    return (
        <Header dividing size='large'>
            {values.name}
        </Header>
    )
}
