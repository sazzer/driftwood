// @flow

import {buildActionName, createAction} from "../redux/actionCreators";
import {createReducer} from "redux-create-reducer";
import produce from "immer";
import {buildSelector} from "../redux/selector";
import {Maybe} from "monet";
import {setAccessToken} from "../api";
import {buildSaga} from "../redux/buildSaga";

/** The namespace for the actions */
const NAMESPACE = 'AUTH/ACCESS_TOKEN';

/** The path to the module */
const MODULE_PATH = ['auth', 'accessToken'];

////////// The actual state

/** The shape of the access token */
export type AccessToken = {
    accessToken: string,
    expires: string,
    userId: string,
};

/** The shape of the state */
type State = {
    token: ?AccessToken,
};

/** The initial state */
export const initialState: State = {};

/**
 * Select the access token, if it's available
 * @param state the state to get the access token from
 * @return The access token
 */
export function selectAccessToken(state: State): ?AccessToken {
    return state.token;
}

/**
 * Select the current user, if it's available
 * @param state the state to get the current user from
 * @return The ID of the current user
 */
export function selectCurrentUser(state: State): ?AccessToken {
    return Maybe.fromUndefined(selectAccessToken(state))
        .map(at => at.userId)
        .orUndefined();
}

////////// Action for storing the access token

/** Action for storing the access token */
const STORE_ACCESS_TOKEN_ACTION = buildActionName('STORE', NAMESPACE);

/** Action Creator for storing the access token */
export const storeAccessToken = createAction(STORE_ACCESS_TOKEN_ACTION);

/** the shape of the Store Access Token action */
type StoreAccessTokenAction = {
    type: string,
    payload: AccessToken,
}

/**
 * Reducer for storing the access token
 * @param state the initial state
 * @return the new state
 */
export function storeAccessTokenReducer(state: State, action: StoreAccessTokenAction) {
    return produce(state, (draft: State) => {
        draft.token = action.payload;
    });
}

/**
 * Saga to set the access token to use for future requests
 * @param action the action
 */
export function setAccessTokenSaga(action: StoreAccessTokenAction) {
    setAccessToken(action.payload.accessToken);
}


////////// Action for logging out

/** Action for logging out */
export const LOGOUT_ACTION = buildActionName('LOGOUT', NAMESPACE);

/** Action Creator for logging out */
export const logout = createAction(LOGOUT_ACTION);

/**
 * Reducer for logging out
 * @param state the initial state
 * @return the new state
 */
export function logoutReducer(state: State) {
    return produce(state, (draft: State) => {
        delete draft.token;
    });
}

/**
 * Saga to clear the access token to use for future requests
 */
export function clearAccessTokenSaga() {
    setAccessToken(null);
}

////////// The actual module definition

/** The reducers for this module */
export const reducers = createReducer(initialState, {
    [STORE_ACCESS_TOKEN_ACTION]: storeAccessTokenReducer,
    [LOGOUT_ACTION]: logoutReducer,
});

/** The sagas for this module */
export const sagas = [
    buildSaga(STORE_ACCESS_TOKEN_ACTION, setAccessTokenSaga),
    buildSaga(LOGOUT_ACTION, clearAccessTokenSaga),
];

/** The actual module */
export default {
    storeAccessToken,
    logout,

    selectAccessToken: buildSelector(MODULE_PATH, selectAccessToken),
    selectCurrentUser: buildSelector(MODULE_PATH, selectCurrentUser),
};
