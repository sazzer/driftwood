// @flow

import React from 'react';
import {Formik} from 'formik';
import ProfilePage from './ProfilePage';

/**
 * Formik wrapper around the Profile Page
 */
export default function FormikProfilePage() {
    const user = {
        name: 'Graham',
        email: 'graham@grahamcox.co.uk',
    };

    return (
        <Formik
            initialValues={user}
            onSubmit={(values, { setSubmitting }) => {
                setTimeout(() => {
                    alert(JSON.stringify(values, null, 2));
                    setSubmitting(false);
                }, 400);
            }}
            render={ProfilePage} />
    )
}
