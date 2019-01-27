// @flow

import {select, call, put} from 'redux-saga/effects';
import {buildActionName, createAction} from "../redux/actionCreators";
import {createReducer} from "redux-create-reducer";
import {buildSaga} from "../redux/buildSaga";
import providers from './providers';
import accessToken from './accessToken';

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

/** The response from starting authentiction */
type StartAuthenticationResponse = {
    accessToken: string,
    expires: string,
    user: string,
    name: string
};

/**
 * Open the Authentication Window and return a promise for the result
 * @param uri the URI to open
 * @return The result
 */
function openAuthenticationWindow(uri: string) {
    return new Promise((resolve, reject) => {
        const eventListener = window.addEventListener('message', (event) => {
            if (event && event.data && event.data.type === 'driftwoodAccessToken') {
                window.removeEventListener('message', eventListener);
                resolve(event.data);
            }
        });

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

    const response: StartAuthenticationResponse = yield call(openAuthenticationWindow, provider.uri);

    yield put(accessToken.storeAccessToken({
        accessToken: response.accessToken,
        expires: response.expires,
        userId: response.user,
    }));
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
