import React from 'react';
import {Grid, Segment, Header, Icon} from 'semantic-ui-react';
import {NamespacesConsumer} from "react-i18next";

/**
 * Screen for when the user is not authenticated but needs to be
 */
export default function NotAuthenticated() {
    return (
        <Grid className='notAuthenticated'>
            <Grid.Row></Grid.Row>
            <Grid.Row>
                <Grid.Column width={4}></Grid.Column>
                <Grid.Column width={8}>
                    <Segment placeholder>
                        <Header icon>
                            <Icon name='lock' />
                            <NamespacesConsumer>{(t) => t('page.notAuthenticated')}</NamespacesConsumer>
                        </Header>
                    </Segment>
                </Grid.Column>
                <Grid.Column width={4}></Grid.Column>
            </Grid.Row>
        </Grid>
    );
}
