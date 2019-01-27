// @flow

import auth from '../authentication/sagas';
import users from '../users/sagas';

/** The set of sagas to use */
export default [
    ...auth,
    ...users,
];
