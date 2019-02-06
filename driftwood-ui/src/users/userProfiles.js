// @flow

import {buildActionName, createAction} from "../redux/actionCreators";
import {createReducer} from "redux-create-reducer";
import produce from "immer";
import {buildSelector} from "../redux/selector";
import {asyncAction, succeededAction} from "../redux/async";
import {request} from "../api";
import {buildSaga} from "../redux/buildSaga";
import {put} from "redux-saga/effects";

/** The namespace for the actions */
const NAMESPACE = 'USERS/USER_PROFILES';

/** The path to the module */
const MODULE_PATH = ['users', 'userProfiles'];

////////// The actual state

export type UserProvider = {
    provider: string,
    providerId: string,
    displayName: string,
};

/** The shape of the user profile */
export type UserProfile = {
    id: string,
    name: string,
    email: ?string,
    providers: ?Array<UserProvider>,
};

/** The shape of the state */
type State = {
    users: { [string] : UserProfile },
};

/** The initial state */
const initialState: State = {
    users: {},
};

/**
 * Select the user profile that has the given ID
 * @param state the state to get the data from
 * @return The user profile
 */
export function selectUserWithId(state: State, id: string): ?UserProfile {
    return state.users[id];
}

////////// Action for starting authentication by a provider

/** Action for storing a user profile */
const STORE_USER_PROFILE_ACTION = buildActionName('STORE', NAMESPACE);

/** Action Creator for storing a user profile */
export const storeUserProfile = createAction(STORE_USER_PROFILE_ACTION);

/** the shape of the Store User Profile action */
type StoreUserProfileAction = {
    type: string,
    payload: UserProfile,
}

/**
 * Reducer for storing a user profile
 * @param state the initial state
 * @return the new state
 */
export function storeUserProfileReducer(state: State, action: StoreUserProfileAction) {
    return produce(state, (draft: State) => {
        draft.users[action.payload.id] = action.payload;
    });
}

////////// Action for loading a user by ID

/** Action for loading a user by ID */
const LOAD_USER_BY_ID_ACTION = buildActionName('LOAD_USER_BY_ID', NAMESPACE);

/** Action Creator for loading a user by ID */
export const loadUserById = createAction(LOAD_USER_BY_ID_ACTION);

/** the shape of the Load User By Id action */
type LoadUserByIdAction = {
    type: string,
    payload: string
};

/** the shape of the Load User By Id Success action */
type LoadUserByIdSuccessAction = {
    type: string,
    payload: {
        result: UserProfile
    }
}

/**
 * Saga to load the providers from the backend
 */
export function* loadUserByIdSaga(action: LoadUserByIdAction): Generator<*, *, *> {
    yield asyncAction(LOAD_USER_BY_ID_ACTION, () =>
        request('/api/users/' + action.payload)
            .then(result => {
                return result.body;
            }));
}

/**
 * Reducer for storing a user profile
 * @param state the initial state
 * @return the new state
 */
export function* loadUserByIdSuccessSaga(action: LoadUserByIdSuccessAction): Generator<*, *, *> {
    yield put(storeUserProfile(action.payload.result));
}


////////// The actual module definition

/** The reducers for this module */
export const reducers = createReducer(initialState, {
    [STORE_USER_PROFILE_ACTION]: storeUserProfileReducer,
});

/** The sagas for this module */
export const sagas = [
    buildSaga(LOAD_USER_BY_ID_ACTION, loadUserByIdSaga),
    buildSaga(succeededAction(LOAD_USER_BY_ID_ACTION), loadUserByIdSuccessSaga),
];

/** The actual module */
export default {
    storeUserProfile,
    loadUserById,

    selectUserWithId: buildSelector(MODULE_PATH, selectUserWithId),

};
