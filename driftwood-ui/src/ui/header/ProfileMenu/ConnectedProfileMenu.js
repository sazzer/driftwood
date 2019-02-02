// @flow

import {connect} from 'react-redux';
import ProfileMenu from './ProfileMenu';
import accessToken from "../../../authentication/accessToken";

/**
 * Map the current store state to the props that this component needs
 */
function mapStateToProps() {
    return {}
}

/**
 * Map the appropriate actions for this component
 * @param dispatch the mechanism to dispatch actions into the store
 * @return the actions that this component needs
 */
function mapDispatchToProps(dispatch) {
    return {
        logout: () => dispatch(accessToken.logout()),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(ProfileMenu);
