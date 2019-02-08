// @flow

import React from 'react';
import {Formik} from 'formik';
import * as Yup from 'yup';
import ProfilePage from './ProfilePage';
import {NamespacesConsumer} from "react-i18next";
import type {UserProfile} from "../../users/userProfiles";

/**
 * The props for the Profile Page component
 */
type FormikProfilePageProps = {
    user: UserProfile,
    userStatus: string,
    errorCode?: string,

    saveUser: (UserProfile) => void,
}

/**
 * Formik wrapper around the Profile Page
 */
export default function FormikProfilePage({user, userStatus, errorCode, saveUser} : FormikProfilePageProps) {
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
                        onSubmit={(values) => {
                            saveUser(values);
                        }}
                        render={props => {
                            return <ProfilePage {...props} errorCode={errorCode} userStatus={userStatus} />;
                        }} />
                )
            }
        </NamespacesConsumer>
    )
}
