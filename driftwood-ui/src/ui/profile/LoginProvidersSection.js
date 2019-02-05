// @flow

import React from 'react';
import {Table} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";

/**
 * Render the login providers
 */
export default function LoginProvidersSection() {
    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Table definition>
                        <Table.Body>
                            <Table.Row>
                                <Table.Cell>{t('profile.page.loginProviders.google')}</Table.Cell>
                                <Table.Cell>graham@grahamcox.co.uk</Table.Cell>
                            </Table.Row>
                        </Table.Body>
                    </Table>
                )
            }
        </NamespacesConsumer>
    )
}
