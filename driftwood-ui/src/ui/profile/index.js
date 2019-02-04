import React from 'react';
import Authenticated from '../authenticated';

/**
 * Wrapper around the user profile to ensure it's only visible when the user is authenticated
 */
export default function() {
    return (
        <Authenticated>
            <div>Hello</div>
        </Authenticated>
    );
};
