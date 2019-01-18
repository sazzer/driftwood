// @flow
import {createReducer} from 'redux-create-reducer';
import produce from 'immer';
import {delay} from 'redux-saga';
import {put} from 'redux-saga/effects';
import {buildSelector} from "../redux/selector";
import {buildSaga} from "../redux/buildSaga";

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
const LOAD_PROVIDERS_ACTION = 'AUTH/LOAD_PROVIDERS';

/** The shape of the Load Providers action */
type LoadProvidersAction = {
    type: string
}

/**
 * Action Creator for loading the providers from the server
 * @return {{type: string}}
 */
export function loadProviders(): LoadProvidersAction {
    return {
        type: LOAD_PROVIDERS_ACTION
    };
}

/**
 * Saga to load the providers from the backend
 */
export function* loadProvidersSaga(): Generator<*, *, *> {
    yield delay(2000);
    yield put({
        type: STORE_PROVIDERS_ACTION,
        payload: ['twitter'],
    });
}

////////// Action for storing the providers into the store

/** Action for storing some providers */
const STORE_PROVIDERS_ACTION = 'AUTH/STORE_PROVIDERS';

/** the shape of the Store Providers action */
type StoreProvidersAction = {
    type: string,
    payload: Array<string>
}

/**
 * Reducer for when we get the Store Providers action
 * @param state the initial state
 * @param action The action
 * @return the new state
 */
export function storeProvidersReducer(state: ProvidersState, action: StoreProvidersAction) {
    return produce(state, (draft: ProvidersState) => {
        draft.providers = action.payload;
    });
}

////////// The actual module definition

/** The reducers for working with providers */
export const reducers = createReducer(initialState, {
    [STORE_PROVIDERS_ACTION]: storeProvidersReducer,
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
