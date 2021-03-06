// @flow

import React from 'react';
import {connect} from 'react-redux';
import ProfilePage from './FormikProfilePage';
import type {UserDetails, UserProfile} from "../../users/userProfiles";
import userProfiles, {USER_PROFILE_PROCESSING} from "../../users/userProfiles";
import accessToken from "../../authentication/accessToken";

/**
 * The props for the Profile Page component
 */
type ConnectedProfilePageProps = {
    currentUserId: string,
    currentUser: UserDetails,

    loadUserById: (string) => void,
    saveUserById: (string, UserProfile) => void,
}

/**
 * Wrapper around the Profile Page component that deals with the Redux Store
 */
class ConnectedProfilePage extends React.Component<ConnectedProfilePageProps> {
    _saveUser = (userProfile: UserProfile) => this.props.saveUserById(this.props.currentUserId, userProfile);

    /**
     * On mounting the component, go and request the current details of the user
     */
    componentDidMount() {
        this.props.loadUserById(this.props.currentUserId);
    }


    /**
     * Actually render the corrected menu
     * @return {*} the menu
     */
    render() {
        return (
            <ProfilePage user={this.props.currentUser.profile}
                         userStatus={this.props.currentUser.status || USER_PROFILE_PROCESSING}
                         errorCode={this.props.currentUser.errorCode}
                         saveUser={this._saveUser} />
        )
    }
}

/**
 * Map the current store state to the props that this component needs
 * @return The props that this component needs
 */
function mapStateToProps(state) {
    const currentUserId = accessToken.selectCurrentUser(state);

    return {
        currentUserId: currentUserId,
        currentUser: userProfiles.selectDetailsWithId(state, currentUserId),
    }
}


/**
 * Map the appropriate actions for this component
 * @param dispatch the mechanism to dispatch actions into the store
 * @return the actions that this component needs
 */
function mapDispatchToProps(dispatch) {
    return {
        loadUserById: (userId) => dispatch(userProfiles.loadUserById(userId)),
        saveUserById: (userId, userProfile) => dispatch(userProfiles.saveUserById({id: userId, user: userProfile})),
    };
}
export default connect(mapStateToProps, mapDispatchToProps)(ConnectedProfilePage);
