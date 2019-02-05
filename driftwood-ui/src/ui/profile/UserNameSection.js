// @flow

import React from 'react';
import {Header} from 'semantic-ui-react';

/** Type describing the user details */
type UserDetails = {
    name?: string,
}

/** Props for the Username Section */
type UserNameSectionProps = {
    values: UserDetails,
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
