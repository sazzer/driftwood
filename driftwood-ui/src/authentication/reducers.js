import {combineReducers} from 'redux';
import {reducers as providersReducers} from './providers';

/** The reducers to use */
export const reducers = combineReducers({
    providers: providersReducers,
});
