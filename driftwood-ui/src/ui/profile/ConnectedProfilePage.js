// @flow

import React from 'react';
import {connect} from 'react-redux';
import ProfilePage from './FormikProfilePage';
import type {UserProfile} from "../../users/userProfiles";
import userProfiles from "../../users/userProfiles";
import accessToken from "../../authentication/accessToken";

/**
 * The props for the Profile Page component
 */
type ConnectedProfilePageProps = {
    currentUser: UserProfile,
}

/**
 * Wrapper around the Profile Page component that deals with the Redux Store
 */
class ConnectedProfilePage extends React.Component<ConnectedProfilePageProps> {
    /**
     * Actually render the corrected menu
     * @return {*} the menu
     */
    render() {
        return (
            <ProfilePage user={this.props.currentUser} />
        )
    }
}

/**
 * Map the current store state to the props that this component needs
 * @return The props that this component needs
 */
function mapStateToProps(state) {
    const currentUser = accessToken.selectCurrentUser(state);

    return {
        currentUser: userProfiles.selectUserWithId(state, currentUser),
    }
}

export default connect(mapStateToProps)(ConnectedProfilePage);
