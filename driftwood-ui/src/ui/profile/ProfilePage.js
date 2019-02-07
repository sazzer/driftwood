// @flow

import React from 'react';
import {Accordion, Button, Container, Form} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";
import BreadcrumbSection from './BreadcrumbSection';
import UserNameSection from './UserNameSection';
import AccountDetailsSection from './AccountDetailsSection';
import LoginProvidersSection from './LoginProvidersSection';
import type {UserProfile} from "../../users/userProfiles";
import {USER_PROFILE_LOADING} from "../../users/userProfiles";

/** Props for the Profile Page */
type ProfilePageProps = {
    userStatus: string,
    values: UserProfile,
    errors: { [string] : string },
    handleChange: () => void,
    handleBlur: () => void,
    handleSubmit: () => void,
    handleReset: () => void,
    dirty: boolean,
}

/**
 * Actually render the profile page
 */
export default function ProfilePage({userStatus, values, handleChange, handleBlur, handleSubmit, handleReset, dirty, errors} : ProfilePageProps) {
    console.log(userStatus);
    const isError = Object.values(errors).length > 0;

    const panels = [
        {
            key: 'accountDetails',
            title: {
                content: <NamespacesConsumer>{(t) => t('profile.page.accountDetails.header')}</NamespacesConsumer>
            },
            content: {
                content: <AccountDetailsSection values={values}
                                                errors={errors}
                                                handleChange={handleChange}
                                                handleBlur={handleBlur}/>
            },
        },
        {
            key: 'loginProviders',
            title: {
                content: <NamespacesConsumer>{(t) => t('profile.page.loginProviders.header')}</NamespacesConsumer>
            },
            content: {
                content: <LoginProvidersSection />
            },
        },
    ];

    return (
        <Container>
            <BreadcrumbSection values={values} />
            <UserNameSection values={values} />

            <Form error={isError} loading={userStatus === USER_PROFILE_LOADING}>
                <Accordion defaultActiveIndex={0} panels={panels} />

                <NamespacesConsumer>
                    {
                        (t) => (
                            <Button.Group>
                                <Button primary type='submit' disabled={!dirty || isError} onClick={handleSubmit}>{t('profile.page.buttons.save')}</Button>
                                <Button.Or text={t('profile.page.buttons.or')}/>
                                <Button negative disabled={!dirty} onClick={handleReset}>{t('profile.page.buttons.cancel')}</Button>
                            </Button.Group>
                        )
                    }
                </NamespacesConsumer>
            </Form>
        </Container>
    );
}
