import {combineReducers} from 'redux';
import {reducers as userProfiles} from './userProfiles';

/** The reducers to use */
export default combineReducers({
    userProfiles,
});
