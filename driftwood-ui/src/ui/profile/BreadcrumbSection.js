// @flow

import React from 'react';
import {Breadcrumb} from 'semantic-ui-react';
import {Link} from 'react-router-dom';
import {NamespacesConsumer, Trans} from "react-i18next";
import type {UserProfile} from "../../users/userProfiles";

/** Props for the Breadcrumb Section */
type BreadcrumbSectionProps = {
    values: UserProfile,
}

/**
 * Render the users breadcrumbs
 */
export default function BreadcrumbSection({values} : BreadcrumbSectionProps) {
    return (
        <NamespacesConsumer>
            {
                (t) => (
                    <Breadcrumb>
                        <Breadcrumb.Section>
                            <Link to='/'>{t('profile.page.breadcrumbs.home')}</Link>
                        </Breadcrumb.Section>
                        <Breadcrumb.Divider icon='right angle'/>
                        <Breadcrumb.Section active>
                            <Trans i18nKey='profile.page.breadcrumbs.user' values={{username: values.name}}>
                                User Profile: <Link to='/profile'>{values.name}</Link>
                            </Trans>
                        </Breadcrumb.Section>
                    </Breadcrumb>
                )
            }
        </NamespacesConsumer>
    )
}
