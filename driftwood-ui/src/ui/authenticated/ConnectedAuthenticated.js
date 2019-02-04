// @flow

import React from 'react';
import {connect} from 'react-redux';
import {Maybe} from 'monet';
import NotAuthenticated from'./NotAuthenticated';
import accessToken from '../../authentication/accessToken';

/**
 * The props for the Not Authenticated component
 */
type ConnectedAuthenticatedProps = {
    currentUser: ?string,
    children: any,
}

/**
 * Wrapper around the Not Authenticated component that deals with the Redux Store
 */
class ConnectedAuthenticated extends React.Component<ConnectedAuthenticatedProps> {
    /**
     * Actually render the corrected menu
     * @return {*} the menu
     */
    render() {
        return Maybe.fromUndefined(this.props.currentUser)
            .map(() => this.props.children)
            .orLazy(() => <NotAuthenticated />);
    }
}

/**
 * Map the current store state to the props that this component needs
 * @return The props that this component needs
 */
function mapStateToProps(state) {
    return {
        currentUser: accessToken.selectCurrentUser(state),
    }
}

export default connect(mapStateToProps)(ConnectedAuthenticated);
