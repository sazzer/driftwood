// @flow

import React from 'react';
import Authenticated from '../authenticated';
import ProfilePage from './ProfilePage';

/**
 * Wrapper around the user profile to ensure it's only visible when the user is authenticated
 */
export default function() {
    return (
        <Authenticated>
            <ProfilePage />
        </Authenticated>
    );
};
