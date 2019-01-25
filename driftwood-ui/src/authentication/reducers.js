import {combineReducers} from 'redux';
import {reducers as providersReducers} from './providers';
import {reducers as authenticateReducers} from './authenticate';

/** The reducers to use */
export default combineReducers({
    providers: providersReducers,
    authenticate: authenticateReducers,
});
