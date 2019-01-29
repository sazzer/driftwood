// @flow

import React from 'react';
import {connect} from "react-redux";
import userProfiles from './userProfiles';
import type {UserProfile} from "./userProfiles";
import {Maybe} from "monet";

/** The props for the Username Consumer */
export type UsernameConsumerProps = {
    user: ?UserProfile,

    children: (?string) => any,
};

/**
 * React HOC that will consume a User ID and make the Name available to the child component
 */
class UsernameConsumer extends React.Component<UsernameConsumerProps> {
    /**
     * Render the component
     */
    render() {
        const user = Maybe.fromUndefined(this.props.user);
        const username = user.map(user => user.name)
            .orUndefined();

        return this.props.children(username);
    }
}

/**
 * Map the current store state to the props that this component needs
 * @return The props that this component needs
 */
function mapStateToProps(state, ownProps) {
    return {
        user: userProfiles.selectUserWithId(state, ownProps.userId),
    }
}

export default connect(mapStateToProps)(UsernameConsumer);
