// @flow

import React from 'react';
import {Formik} from 'formik';
import * as Yup from 'yup';
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
            enableReinitialize={true}
            validationSchema={Yup.object().shape({
                name: Yup.string().required('Name is required'),
                email: Yup.string().email('Email Address is not valid'),
            })}
            onSubmit={(values, { setSubmitting }) => {
                setTimeout(() => {
                    alert(JSON.stringify(values, null, 2));
                    setSubmitting(false);
                }, 400);
            }}
            render={ProfilePage} />
    )
}
