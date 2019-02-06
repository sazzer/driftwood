// @flow

import React from 'react';
import {Formik} from 'formik';
import * as Yup from 'yup';
import ProfilePage from './ProfilePage';
import {NamespacesConsumer} from "react-i18next";

/**
 * Formik wrapper around the Profile Page
 */
export default function FormikProfilePage() {
    const user = {
        name: 'Graham',
        email: 'graham@grahamcox.co.uk',
    };

    return (
        <NamespacesConsumer>
            {
                (t) => (

                    <Formik
                        initialValues={user}
                        enableReinitialize={true}
                        validationSchema={Yup.object().shape({
                            name: Yup.string().required(t('profile.page.accountDetails.screenName.errors.required')),
                            email: Yup.string().email(t('profile.page.accountDetails.email.errors.email')),
                        })}
                        validateOnChange={false}
                        onSubmit={(values, {setSubmitting}) => {
                            setTimeout(() => {
                                alert(JSON.stringify(values, null, 2));
                                setSubmitting(false);
                            }, 400);
                        }}
                        render={ProfilePage}/>
                )
            }
        </NamespacesConsumer>
    )
}
