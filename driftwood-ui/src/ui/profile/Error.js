// @flow

import React from 'react';
import {Message} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";

/** Props for the Error  */
type ErrorProps = {
    errorCode: string,
}

/**
 * Render the users breadcrumbs
 */
export default function Error({errorCode} : ErrorProps) {
    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Message error errorCode={errorCode}>
                        {t(`profile.page.errors.${errorCode.replace(/\./g, '_')}`)}
                    </Message>
                )
            }
        </NamespacesConsumer>
    )
}
