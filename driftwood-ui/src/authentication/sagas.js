// @flow

import {sagas as providers} from './providers';
import {sagas as authenticate} from './authenticate';
import {sagas as accessToken} from './accessToken';
import {sagas as rememberLogin} from './rememberLogin';

/** The sagas to work with */
export default [
    ...providers,
    ...authenticate,
    ...accessToken,
    ...rememberLogin,
]
