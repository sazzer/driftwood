// @flow

import {buildActionName, createAction} from "../redux/actionCreators";
import {createReducer} from "redux-create-reducer";
import produce from "immer";

/** The namespace for the actions */
const NAMESPACE = 'AUTH/ACCESS_TOKEN';

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

////////// The actual module definition

/** The reducers for this module */
export const reducers = createReducer(initialState, {
    [STORE_ACCESS_TOKEN_ACTION]: storeAccessTokenReducer,
});

/** The sagas for this module */
export const sagas = [
];

/** The actual module */
export default {
    storeAccessToken,
};
