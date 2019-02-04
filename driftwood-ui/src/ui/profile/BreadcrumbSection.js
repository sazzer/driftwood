import React from 'react';
import {Breadcrumb} from 'semantic-ui-react';
import {Link} from 'react-router-dom';
import {NamespacesConsumer, Trans} from "react-i18next";

/**
 * Render the users breadcrumbs
 */
export default function BreadcrumbSection() {
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
                            <Trans i18nKey='profile.page.breadcrumbs.user' values={{username: 'Graham'}}>
                                User Profile: <Link to='/profile'>Graham</Link>
                            </Trans>
                        </Breadcrumb.Section>
                    </Breadcrumb>
                )
            }
        </NamespacesConsumer>
    )
}
