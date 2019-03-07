// @flow

import React from 'react';
import {Form, Message} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";
import type {UserProfile} from "../../users/userProfiles";

/** Props for the Account Details Section */
type AccountDetailsSectionProps = {
    values: UserProfile,
    errors: { [string] : string },
    handleChange: () => void,
    handleBlur: () => void,
}

/**
 * Render the account details
 */
export default function AccountDetailsSection({values, errors, handleChange, handleBlur}: AccountDetailsSectionProps) {
    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <>
                        <Form.Input label={t('profile.page.accountDetails.screenName.label')}
                                    placeholder={t('profile.page.accountDetails.screenName.placeholder')}
                                    required
                                    name='name'
                                    error={errors.name !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    value={values.name || ''} />
                        <Message attached='bottom' error data-test="nameMessages">{errors.name}</Message>
                        <Form.Input label={t('profile.page.accountDetails.email.label')}
                                    placeholder={t('profile.page.accountDetails.email.placeholder')}
                                    name='email'
                                    error={errors.email !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    value={values.email || ''} />
                        <Message attached='bottom' error data-test="emailMessages">{errors.email}</Message>
                    </>
                )
            }
        </NamespacesConsumer>
    )
}
