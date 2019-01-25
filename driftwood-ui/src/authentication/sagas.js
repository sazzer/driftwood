// @flow

import {sagas as providerSagas} from './providers';
import {sagas as authenticateSagas} from './authenticate';

/** The sagas to work with */
export default [
    ...providerSagas,
    ...authenticateSagas,
]
