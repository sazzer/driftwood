// @flow
import {createReducer} from 'redux-create-reducer';
import produce from 'immer';
import {buildSelector} from "../redux/selector";

const LOAD_PROVIDERS_ACTION = 'AUTH/LOAD_PROVIDERS';

/** The shape of the Providers state */
type PROVIDERS_STATE = {
    providers: Array<string>,
};

/** The initial state */
const initialState: PROVIDERS_STATE = {
    providers: []
};

/**
 * Action Creator for loading the providers from the server
 * @return {{type: string}}
 */
export function loadProviders() {
    return {
        type: LOAD_PROVIDERS_ACTION
    };
}

/**
 * Select the providers that are available
 * @param state the state to get the providers from
 * @return The providers
 */
export function selectProviders(state: PROVIDERS_STATE) {
    return state.providers;
}

/**
 * Reducer for when we get the Load Providers action
 * @param state the initial state
 * @return the new state
 */
export function loadProvidersReducer(state: PROVIDERS_STATE) {
    return produce(state, (draft: PROVIDERS_STATE) => {
        draft.providers = ['facebook'];
    });
}

/** The reducers for working with providers */
export const reducers = createReducer(initialState, {
    [LOAD_PROVIDERS_ACTION]: loadProvidersReducer,
});

/** The actual providers module */
export default {
    loadProviders,

    selectProviders: buildSelector(['auth', 'providers'], selectProviders),
};
