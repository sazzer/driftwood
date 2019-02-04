import React from 'react';
import {Container, Accordion, Button} from 'semantic-ui-react';
import BreadcrumbSection from './BreadcrumbSection';
import UserNameSection from './UserNameSection';
import AccountDetailsSection from './AccountDetailsSection';
import LoginProvidersSection from './LoginProvidersSection';
import {NamespacesConsumer} from "react-i18next";

/**
 * Actually render the profile page
 */
export default function ProfilePage() {
    const panels = [
        {
            key: 'accountDetails',
            title: {
                content: <NamespacesConsumer>{(t) => t('profile.page.accountDetails.header')}</NamespacesConsumer>
            },
            content: {
                content: <AccountDetailsSection />
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
            <BreadcrumbSection />
            <UserNameSection />

            <Accordion defaultActiveIndex={0} panels={panels} />

            <NamespacesConsumer>
                {
                    (t) => (
                        <Button.Group>
                            <Button primary disabled>{t('profile.page.buttons.save')}</Button>
                            <Button.Or text={t('profile.page.buttons.or')}/>
                            <Button negative disabled>{t('profile.page.buttons.cancel')}</Button>
                        </Button.Group>
                    )
                }
            </NamespacesConsumer>
        </Container>
    )
}
