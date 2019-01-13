// @flow

import {createSagas} from 'redux-box';

/** The shape of the state for this module */
type DummyModuleState = {};

/** The name of this Redux Box module */
const MODULE_NAME = 'dummy';

/** The initial state for this module */
const initialState: DummyModuleState = {
};

/** The actions for this module */
const actions = {
};

/** The mutations for this module */
const mutations = {
};

/**
 * Dummy Saga
 */
export function dummySaga() {

}

/** The sagas for this module */
const sagas = {
    testing: dummySaga
};

/** The selectors for this module */
const selectors = {
};

/** The actual module */
export const module = {
    name: MODULE_NAME,
    state: initialState,
    actions,
    mutations,
    sagas: createSagas(sagas),
    selectors
};

/** The shape of this module */
export type DummyModule = {};
