// @flow

import {buildActionName, createAction} from "../redux/actionCreators";
import {createReducer} from "redux-create-reducer";
import produce from "immer";
import {buildSelector} from "../redux/selector";
import {asyncAction, failedAction, startedAction, succeededAction} from "../redux/async";
import {request} from "../api";
import {buildSaga} from "../redux/buildSaga";
import {put} from "redux-saga/effects";
import {Maybe} from "monet";
import type {Problem} from "../api/problem";

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

/** Status string to indicate that a user profile is currently loading or saving */
export const USER_PROFILE_PROCESSING = 'PROCESSING';

/** Status string to indicate that a user profile has been loaded successfully */
export const USER_PROFILE_LOADED = 'LOADED';

/** Status string to indicate that a user profile failed to load or save */
export const USER_PROFILE_FAILED = 'FAILED';

/** The details of a user */
export type UserDetails = {
    profile: UserProfile,
    status?: string,
    errorCode?: string,
}
/** The shape of the state */
type State = {
    users: { [string] : UserDetails },
};

/** The initial state */
const initialState: State = {
    users: {},
};

/**
 * Select the user profile that has the given ID
 * @param state the state to get the data from
 * @param id The id of the user
 * @return The user profile
 */
export function selectUserWithId(state: State, id: string): ?UserProfile {
    return Maybe.fromUndefined(state.users[id])
        .map(user => user.profile)
        .orUndefined();
}

/**
 * Select the details of the user profile that has the given ID
 * @param state the state to get the data from
 * @param id The id of the user
 * @return The status
 */
export function selectDetailsWithId(state: State, id: string): ?UserDetails {
    return Maybe.fromUndefined(state.users[id])
        .orUndefined();
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
 * @param action The action
 * @return the new state
 */
export function storeUserProfileReducer(state: State, action: StoreUserProfileAction) {
    return produce(state, (draft: State) => {
        draft.users[action.payload.id] = {
            profile: action.payload,
            status: USER_PROFILE_LOADED,
        };
    });
}

////////// Common details between loading and saving a user

/** the shape of the Process User By Id Started action */
type ProcessUserByIdStartedAction = {
    type: string,
    input: Array<string>,
};

/** the shape of the Load User By Id Failed action */
type ProcessUserByIdFailedAction = {
    type: string,
    input: Array<string>,
    payload: {
        error: Problem
    },
};

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
    },
    input: Array<string>,
};

/**
 * Reducer for storing that we are starting to load or save a user profile
 * @param state the initial state
 * @param action The action
 * @return the new state
 */
export function startProcessingUserProfileReducer(state: State, action: ProcessUserByIdStartedAction) {
    return produce(state, (draft: State) => {
        const userId = action.input[0];
        if (draft.users[userId] === undefined) {
            draft.users[userId] = {};
        }
        draft.users[userId].status = USER_PROFILE_PROCESSING;
    });
}

/**
 * Reducer for storing that we failed to load or save a user profile
 * @param state the initial state
 * @param action The action
 * @return the new state
 */
export function failedProcessingUserProfileReducer(state: State, action: ProcessUserByIdFailedAction) {
    return produce(state, (draft: State) => {
        const userId = action.input[0];
        if (draft.users[userId] === undefined) {
            draft.users[userId] = {};
        }
        draft.users[userId].status = USER_PROFILE_FAILED;
        draft.users[userId].errorCode = action.payload.error.type;
    });
}

/**
 * Saga to load the providers from the backend
 */
export function* loadUserByIdSaga(action: LoadUserByIdAction): Generator<*, *, *> {
    yield asyncAction(LOAD_USER_BY_ID_ACTION, (userId: string) =>
        request('/api/users/' + userId)
            .then(result => {
                if (result.status === 200) {
                    return result.body;
                } else {
                    throw result.body;
                }
            }), action.payload);
}

/**
 * Reducer for storing a user profile
 * @param action the action
 * @return the new state
 */
export function* processUserByIdSuccessSaga(action: LoadUserByIdSuccessAction): Generator<*, *, *> {
    yield put(storeUserProfile(action.payload.result));
}

////////// Action for saving a user by ID

/** Action for saving a user by ID */
const SAVE_USER_BY_ID_ACTION = buildActionName('SAVE_USER_BY_ID', NAMESPACE);

/** Action Creator for saving a user by ID */
export const saveUserById = createAction(SAVE_USER_BY_ID_ACTION);

/** the shape of the Save User By Id action */
type SaveUserByIdAction = {
    type: string,
    payload: {
        id: string,
        user: UserProfile
    }
};

/**
 * Saga to save the providers from the backend
 */
export function* saveUserByIdSaga(action: SaveUserByIdAction): Generator<*, *, *> {
    yield asyncAction(SAVE_USER_BY_ID_ACTION, () =>
        request('/api/users/' + action.payload.id, {method: 'put', data: action.payload.user})
            .then(result => {
                if (result.status === 200) {
                    return result.body;
                } else {
                    throw result.body;
                }
            }), action.payload.id);
}

////////// The actual module definition

/** The reducers for this module */
export const reducers = createReducer(initialState, {
    [STORE_USER_PROFILE_ACTION]: storeUserProfileReducer,

    [startedAction(LOAD_USER_BY_ID_ACTION)]: startProcessingUserProfileReducer,
    [failedAction(LOAD_USER_BY_ID_ACTION)]: failedProcessingUserProfileReducer,

    [startedAction(SAVE_USER_BY_ID_ACTION)]: startProcessingUserProfileReducer,
    [failedAction(SAVE_USER_BY_ID_ACTION)]: failedProcessingUserProfileReducer,
});

/** The sagas for this module */
export const sagas = [
    buildSaga(LOAD_USER_BY_ID_ACTION, loadUserByIdSaga),
    buildSaga(succeededAction(LOAD_USER_BY_ID_ACTION), processUserByIdSuccessSaga),

    buildSaga(SAVE_USER_BY_ID_ACTION, saveUserByIdSaga),
    buildSaga(succeededAction(SAVE_USER_BY_ID_ACTION), processUserByIdSuccessSaga),
];

/** The actual module */
export default {
    storeUserProfile,
    loadUserById,
    saveUserById,

    selectUserWithId: buildSelector(MODULE_PATH, selectUserWithId),
    selectDetailsWithId: buildSelector(MODULE_PATH, selectDetailsWithId),

};
