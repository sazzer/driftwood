// @flow

import {put} from 'redux-saga/effects';
import {buildActionName, createAction} from "../redux/actionCreators";
import {createReducer} from "redux-create-reducer";
import accessToken, {LOGOUT_ACTION} from './accessToken';
import userProfiles from "../users/userProfiles";
import {buildSaga} from "../redux/buildSaga";

/** The namespace for the actions */
const NAMESPACE = 'AUTH/REMEMBER_LOGIN';

////////// The actual state

/** The shape of the state */
type State = {};

/** The initial state */
export const initialState: State = {};

////////// Action for storing the authentication details

/** Action for storing the access token */
const STORE_AUTHENTICATION_ACTION = buildActionName('STORE', NAMESPACE);

/** Action Creator for storing the access token */
export const storeAuthentication = createAction(STORE_AUTHENTICATION_ACTION);

/** The payload for the Authentication action */
type Authentication = {
    accessToken: string,
    expires: string,
    user: string,
    name: string
};

/** the shape of the Store Authentication action */
type StoreAuthenticationAction = {
    type: string,
    payload: Authentication,
}

/**
 * Saga to store authentication
 */
export function* storeAuthenticationSaga(action: StoreAuthenticationAction): Generator<*, *, *> {
    if (window.sessionStorage) {
        window.sessionStorage.setItem('driftwood_authentication', JSON.stringify(action.payload));
    }

    yield put(userProfiles.storeUserProfile({
        id: action.payload.user,
        name: action.payload.name,
    }));

    yield put(accessToken.storeAccessToken({
        accessToken: action.payload.accessToken,
        expires: action.payload.expires,
        userId: action.payload.user,
    }));
}

////////// Saga for logging in on application start

export function* autoLogin(): Generator<*, *, *> {
    if (window.sessionStorage) {
        const loginDetails = window.sessionStorage.getItem('driftwood_authentication');
        if (loginDetails) {
            yield put(storeAuthentication(JSON.parse(loginDetails)));
        }
    }
}

////////// Saga for logging out

export function logout() {
    if (window.sessionStorage) {
        window.sessionStorage.removeItem('driftwood_authentication');
    }

}

////////// The actual module definition

/** The reducers for this module */
export const reducers = createReducer(initialState, {
});

/** The sagas for this module */
export const sagas = [
    buildSaga(STORE_AUTHENTICATION_ACTION, storeAuthenticationSaga),
    buildSaga(LOGOUT_ACTION, logout),
    autoLogin,
];

/** The actual module */
export default {
    storeAuthentication,
};
