// @flow
import {createReducer} from 'redux-create-reducer';
import produce from 'immer';
import {buildSelector} from "../redux/selector";
import {buildSaga} from "../redux/buildSaga";
import {buildActionName, createAction} from "../redux/actionCreators";
import {asyncAction, failedAction, startedAction, succeededAction} from "../redux/async";

/** The namespace for the actions */
const NAMESPACE = 'AUTH';

/** The path to the module */
const MODULE_PATH = ['auth', 'providers'];

/** Provider State to indicate that the providers are loading */
export const PROVIDERS_STATE_LOADING = 'loading';

/** Provider State to indicate that the providers are loaded successfully */
export const PROVIDERS_STATE_LOADED = 'loaded';

/** Provider State to indicate that the providers failed to load */
export const PROVIDERS_STATE_FAILED = 'failed';

////////// The actual state

/** The shape of the Providers state */
type ProvidersState = {
    providers: Array<string>,
    state?: string,
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
export function selectProviders(state: ProvidersState): Array<string> {
    return state.providers;
}

/**
 * Select the load state of the providers
 * @param state the state to get the providers from
 * @return The load state
 */
export function selectProviderLoadState(state: ProvidersState): ?string {
    return state.state;
}

////////// Action for requesting that the providers are loaded

/** Action for loading some providers */
const LOAD_PROVIDERS_ACTION = buildActionName('LOAD_PROVIDERS', NAMESPACE);

/** Action Creator for loading the providers from the server */
export const loadProviders = createAction(LOAD_PROVIDERS_ACTION);

/**
 * Saga to load the providers from the backend
 */
export function* loadProvidersSaga(): Generator<*, *, *> {
    yield asyncAction(STORE_PROVIDERS_ACTION, (a, b) => {
        return new Promise((resolve) => {
            setTimeout(() => resolve([b, a]), 2000);
        });
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
 * Reducer for when we start the Store Providers action
 * @param state the initial state
 * @return the new state
 */
export function storeProvidersStartedReducer(state: ProvidersState) {
    return produce(state, (draft: ProvidersState) => {
        draft.state = PROVIDERS_STATE_LOADING;
    });
}
/**
 * Reducer for when we succeed with the Store Providers action
 * @param state the initial state
 * @param action The action
 * @return the new state
 */
export function storeProvidersSuccessReducer(state: ProvidersState, action: StoreProvidersSuccessAction) {
    return produce(state, (draft: ProvidersState) => {
        draft.providers = action.payload.result;
        draft.state = PROVIDERS_STATE_LOADED;
    });
}
/**
 * Reducer for when we fail the Store Providers action
 * @param state the initial state
 * @param action The action
 * @return the new state
 */
export function storeProvidersFailedReducer(state: ProvidersState, action: StoreProvidersSuccessAction) {
    return produce(state, (draft: ProvidersState) => {
        draft.state = PROVIDERS_STATE_FAILED;
    });
}

////////// The actual module definition

/** The reducers for working with providers */
export const reducers = createReducer(initialState, {
    [startedAction(STORE_PROVIDERS_ACTION)]: storeProvidersStartedReducer,
    [succeededAction(STORE_PROVIDERS_ACTION)]: storeProvidersSuccessReducer,
    [failedAction(STORE_PROVIDERS_ACTION)]: storeProvidersFailedReducer,
});

/** The sagas for working with providers */
export const sagas = [
    buildSaga(LOAD_PROVIDERS_ACTION, loadProvidersSaga),
];

/** The actual providers module */
export default {
    loadProviders,

    selectProviders: buildSelector(MODULE_PATH, selectProviders),
    selectProviderLoadState: buildSelector(MODULE_PATH, selectProviderLoadState),
};
