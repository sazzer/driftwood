// @flow

import React from 'react';
import {Table} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";
import type {UserProfile} from "../../users/userProfiles";
import {Maybe} from "monet";

/** Props for the Profile Page */
type LoginProvidersSectionProps = {
    values: UserProfile,
}
/**
 * Render the login providers
 */
export default function LoginProvidersSection({values} : LoginProvidersSectionProps) {
    return (
        <NamespacesConsumer>
            {
                (t) => {
                    const rows = Maybe.fromUndefined(values.logins)
                        .orSome([])
                        .map(provider => (
                            <Table.Row key={`${provider.provider}-${provider.providerId}`}>
                                <Table.Cell>{t(`profile.page.loginProviders.${provider.provider}`)}</Table.Cell>
                                <Table.Cell>{provider.displayName}</Table.Cell>
                            </Table.Row>
                        ));

                    return (
                        <Table definition>
                            <Table.Body>
                                {rows}
                            </Table.Body>
                        </Table>
                    );
                }
            }
        </NamespacesConsumer>
    )
}
