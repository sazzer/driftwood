// @flow

import React from 'react';
import {Container, Accordion, Button} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";
import { Formik } from 'formik';
import BreadcrumbSection from './BreadcrumbSection';
import UserNameSection from './UserNameSection';
import AccountDetailsSection from './AccountDetailsSection';
import LoginProvidersSection from './LoginProvidersSection';

/**
 * Actually render the profile page
 */
export default function ProfilePage() {
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
            }}>
            {({
                  values,
                  handleChange,
                  handleBlur,
                  handleSubmit,
                  handleReset,
                  dirty,
              }) => {
                const panels = [
                    {
                        key: 'accountDetails',
                        title: {
                            content: <NamespacesConsumer>{(t) => t('profile.page.accountDetails.header')}</NamespacesConsumer>
                        },
                        content: {
                            content: <AccountDetailsSection values={values} handleChange={handleChange} handleBlur={handleBlur}/>
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

                        <Accordion defaultActiveIndex={0} panels={panels} />

                        <NamespacesConsumer>
                            {
                                (t) => (
                                    <Button.Group>
                                        <Button primary disabled={!dirty} onClick={handleSubmit}>{t('profile.page.buttons.save')}</Button>
                                        <Button.Or text={t('profile.page.buttons.or')}/>
                                        <Button negative disabled={!dirty} onClick={handleReset}>{t('profile.page.buttons.cancel')}</Button>
                                    </Button.Group>
                                )
                            }
                        </NamespacesConsumer>
                    </Container>
                );
            }
            }
        </Formik>
    )
}
