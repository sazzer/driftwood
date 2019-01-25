// @flow

import {select, call} from 'redux-saga/effects';
import {buildActionName, createAction} from "../redux/actionCreators";
import {createReducer} from "redux-create-reducer";
import {buildSaga} from "../redux/buildSaga";
import providers from './providers';

/** The namespace for the actions */
const NAMESPACE = 'AUTH/AUTHENTICATE';

////////// The actual state

/** The shape of the state */
type State = {};

/** The initial state */
const initialState: State = {};

////////// Action for starting authentication by a provider

/** Action for starting authentication */
const START_AUTH_ACTION = buildActionName('START', NAMESPACE);

/** Action Creator for starting authentication */
export const startAuthentication = createAction(START_AUTH_ACTION);

/** the shape of the Start Authentication Success action */
type StartAuthenticationAction = {
    type: string,
    payload: string
}

/**
 * Open the Authentication Window and return a promise for the result
 * @param uri the URI to open
 * @return The result
 */
function openAuthenticationWindow(uri: string) {
    return new Promise((resolve, reject) => {
        window.open(uri,
            'driftwoodLoginWindow',
            'centerscreen,menubar=no,toolbar=no,location,personalbar=no,status,dependent,resizable,scrollbars');
    });
}
/**
 * Saga to starting authentication
 */
export function* startAuthenticationSaga(action: StartAuthenticationAction): Generator<*, *, *> {
    const provider = yield select(providers.selectProviderById, action.payload);

    yield call(openAuthenticationWindow, provider.uri);
}

////////// The actual module definition

/** The reducers for this module */
export const reducers = createReducer(initialState, {
});

/** The sagas for this module */
export const sagas = [
    buildSaga(START_AUTH_ACTION, startAuthenticationSaga),
];

/** The actual module */
export default {
    startAuthentication,
};
