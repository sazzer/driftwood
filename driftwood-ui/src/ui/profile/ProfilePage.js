// @flow

import React, {useState} from 'react';
import {Accordion, Button, Container, Form, Icon} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";
import BreadcrumbSection from './BreadcrumbSection';
import UserNameSection from './UserNameSection';
import AccountDetailsSection from './AccountDetailsSection';
import LoginProvidersSection from './LoginProvidersSection';
import type {UserProfile} from "../../users/userProfiles";
import {USER_PROFILE_PROCESSING} from "../../users/userProfiles";
import Error from "./Error";

/** Props for the Profile Page */
type ProfilePageProps = {
    userStatus: string,
    errorCode?: string,
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
export default function ProfilePage({userStatus, errorCode, values, handleChange, handleBlur, handleSubmit, handleReset, dirty, errors} : ProfilePageProps) {
    const isError = errorCode !== undefined || Object.values(errors).length > 0;

    const [openAccordion, setOpenAccordion] = useState(0);

    const handleOpenAccordion = (e, {index}) => setOpenAccordion(index);

    return (
        <Container data-test="userProfile">
            <BreadcrumbSection values={values} />
            <UserNameSection values={values} />
            { errorCode && <Error errorCode={errorCode} />}

            <Form error={isError} loading={userStatus === USER_PROFILE_PROCESSING}>
                <NamespacesConsumer>
                    {
                        (t) => (
                            <>
                                <Accordion styles>
                                    <Accordion.Title active={openAccordion === 0} index={0} onClick={handleOpenAccordion} data-test="accountDetailsHeader">
                                        <Icon name="dropdown" />
                                        {t('profile.page.accountDetails.header')}
                                    </Accordion.Title>
                                    <Accordion.Content active={openAccordion === 0} data-test="accountDetailsContent">
                                        <AccountDetailsSection values={values}
                                                               errors={errors}
                                                               handleChange={handleChange}
                                                               handleBlur={handleBlur}/>
                                    </Accordion.Content>
                                    <Accordion.Title active={openAccordion === 1} index={1} onClick={handleOpenAccordion}  data-test="loginProvidersHeader">
                                        <Icon name="dropdown" />
                                        {t('profile.page.loginProviders.header')}
                                    </Accordion.Title>
                                    <Accordion.Content active={openAccordion === 1} data-test="loginProvidersContent">
                                        <LoginProvidersSection values={values} />
                                    </Accordion.Content>
                                </Accordion>

                                <Button.Group>
                                    <Button primary type='submit' disabled={!dirty || isError} onClick={handleSubmit}>{t('profile.page.buttons.save')}</Button>
                                    <Button.Or text={t('profile.page.buttons.or')}/>
                                    <Button negative disabled={!dirty} onClick={handleReset}>{t('profile.page.buttons.cancel')}</Button>
                                </Button.Group>
                            </>
                        )
                    }
                </NamespacesConsumer>
            </Form>
        </Container>
    );
}
