// @flow
import {createReducer} from 'redux-create-reducer';
import produce from 'immer';
import {buildSelector} from "../redux/selector";
import {buildSaga} from "../redux/buildSaga";
import {buildActionName, createAction} from "../redux/actionCreators";
import {asyncAction, succeededAction} from "../redux/async";

/** The namespace for the actions */
const NAMESPACE = 'AUTH';

////////// The actual state

/** The shape of the Providers state */
type ProvidersState = {
    providers: Array<string>,
};

/** The initial state */
const initialState: ProvidersState = {
    providers: []
};

/**
 * Select the providers that are available
 * @param state the state to get the providers from
 * @return The providers
 */
export function selectProviders(state: ProvidersState) {
    return state.providers;
}

////////// Action for requesting that the providers are loaded

/** Action for loading some providers */
const LOAD_PROVIDERS_ACTION = buildActionName('LOAD_PROVIDERS', NAMESPACE);

/**
 * Action Creator for loading the providers from the server
 * @return {{type: string}}
 */
export const loadProviders = createAction(LOAD_PROVIDERS_ACTION);

/**
 * Saga to load the providers from the backend
 */
export function* loadProvidersSaga(): Generator<*, *, *> {
    yield asyncAction(STORE_PROVIDERS_ACTION, async (a, b) => {
        return [b, a]
    }, 'google', 'twitter');
}

////////// Action for storing the providers into the store

/** Action for storing some providers */
const STORE_PROVIDERS_ACTION = buildActionName('STORE_PROVIDERS', NAMESPACE);

/** the shape of the Store Providers Success action */
type StoreProvidersSuccessAction = {
    type: string,
    payload: {
        result: Array<string>
    }}

/**
 * Reducer for when we get the Store Providers action
 * @param state the initial state
 * @param action The action
 * @return the new state
 */
export function storeProvidersReducer(state: ProvidersState, action: StoreProvidersSuccessAction) {
    return produce(state, (draft: ProvidersState) => {
        draft.providers = action.payload.result;
    });
}

////////// The actual module definition

/** The reducers for working with providers */
export const reducers = createReducer(initialState, {
    [succeededAction(STORE_PROVIDERS_ACTION)]: storeProvidersReducer,
});

/** The sagas for working with providers */
export const sagas = [
    buildSaga(LOAD_PROVIDERS_ACTION, loadProvidersSaga),
];

/** The actual providers module */
export default {
    loadProviders,

    selectProviders: buildSelector(['auth', 'providers'], selectProviders),
};
