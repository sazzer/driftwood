import React from 'react';
import {Form} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";

/**
 * Render the account details
 */
export default function AccountDetailsSection() {
    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Form>
                        <Form.Field required>
                            <label>{t('profile.page.accountDetails.screenName.label')}</label>
                            <input placeholder={t('profile.page.accountDetails.screenName.placeholder')} value='Graham'/>
                        </Form.Field>
                        <Form.Field>
                            <label>{t('profile.page.accountDetails.email.label')}</label>
                            <input placeholder={t('profile.page.accountDetails.email.placeholder')}/>
                        </Form.Field>
                    </Form>
                )
            }
        </NamespacesConsumer>
    )
}
