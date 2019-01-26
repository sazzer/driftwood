import {combineReducers} from 'redux';
import {reducers as providers} from './providers';
import {reducers as authenticate} from './authenticate';
import {reducers as accessToken} from './accessToken';

/** The reducers to use */
export default combineReducers({
    providers,
    authenticate,
    accessToken
});
