// @flow

import {buildActionName, createAction} from "../redux/actionCreators";
import {createReducer} from "redux-create-reducer";
import {buildSaga} from "../redux/buildSaga";

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
 * Saga to starting authentication
 */
export function* startAuthenticationSaga(action: StartAuthenticationAction): Generator<*, *, *> {
    console.log(action);
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
