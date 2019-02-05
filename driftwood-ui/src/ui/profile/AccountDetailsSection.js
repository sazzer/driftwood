// @flow

import React from 'react';
import {Form} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";

/** Type describing the user details */
type UserDetails = {
    name?: string,
    email?: string,
}

/** Props for the Account Details Section */
type AccountDetailsSectionProps = {
    values: UserDetails,
    handleChange: () => void,
    handleBlur: () => void,
}

/**
 * Render the account details
 */
export default function AccountDetailsSection({values, handleChange, handleBlur}: AccountDetailsSectionProps) {
    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Form>
                        <Form.Field required>
                            <label>{t('profile.page.accountDetails.screenName.label')}</label>
                            <input placeholder={t('profile.page.accountDetails.screenName.placeholder')}
                                   name='name'
                                   onChange={handleChange}
                                   onBlur={handleBlur}
                                   value={values.name}/>
                        </Form.Field>
                        <Form.Field>
                            <label>{t('profile.page.accountDetails.email.label')}</label>
                            <input placeholder={t('profile.page.accountDetails.email.placeholder')}
                                   name='email'
                                   onChange={handleChange}
                                   onBlur={handleBlur}
                                   value={values.email}/>
                        </Form.Field>
                    </Form>
                )
            }
        </NamespacesConsumer>
    )
}
