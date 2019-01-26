// @flow

import {sagas as providers} from './providers';
import {sagas as authenticate} from './authenticate';
import {sagas as accessToken} from './accessToken';

/** The sagas to work with */
export default [
    ...providers,
    ...authenticate,
    ...accessToken,
]
