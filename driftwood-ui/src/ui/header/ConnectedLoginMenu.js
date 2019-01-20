// @flow

import React from 'react';
import {connect} from 'react-redux';
import providers from '../../authentication/providers';
import LoginMenu, {LoginMenuStatus} from './LoginMenu';

/**
 * The props for the Login Menu component
 */
type ConnectedLoginMenuProps = {
    loadProviders: () => void,
    providers: Array<string>,
    status?: Symbol,
}

/**
 * Wrapper around the Login Menu component that deals with the Redux Store
 */
class ConnectedLoginMenu extends React.Component<ConnectedLoginMenuProps> {
    /**
     * On mounting the component, go and request that the providers are loaded from the backend
     */
    componentDidMount() {
        this.props.loadProviders();
    }

    /**
     * Actually render the menu with the providers that have been loaded
     * @return {*} the login menu
     */
    render() {
        return <LoginMenu providers={this.props.providers} status={this.props.status} />
    }
}

/**
 * Map the current store state to the props that this component needs
 * @return The props that this component needs
 */
function mapStateToProps(state) {
    return {
        providers: providers.selectProviders(state),
        status: LoginMenuStatus[providers.selectProviderLoadState(state)],
    }
}

/**
 * Map the appropriate actions for this component
 * @param dispatch the mechanism to dispatch actions into the store
 * @return the actions that this component needs
 */
function mapDispatchToProps(dispatch) {
    return {
        loadProviders: () => dispatch(providers.loadProviders()),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(ConnectedLoginMenu);
