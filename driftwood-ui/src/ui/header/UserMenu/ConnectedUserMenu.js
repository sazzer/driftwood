// @flow

import React from 'react';
import {connect} from 'react-redux';
import {Maybe} from "monet";
import LoginMenu from '../LoginMenu';
import ProfileMenu from '../ProfileMenu';
import accessToken from '../../../authentication/accessToken';

/**
 * The props for the User Menu component
 */
type ConnectedUserMenuProps = {
    currentUser: ?string,
}

/**
 * Wrapper around the User Menu component that deals with the Redux Store
 */
class ConnectedUserMenu extends React.Component<ConnectedUserMenuProps> {
    /**
     * Actually render the corrected menu
     * @return {*} the menu
     */
    render() {
        return Maybe.fromUndefined(this.props.currentUser)
            .map((userId) => <ProfileMenu userId={userId} />)
            .orLazy(() => <LoginMenu />);
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

export default connect(mapStateToProps)(ConnectedUserMenu);
