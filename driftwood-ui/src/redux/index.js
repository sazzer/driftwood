// @flow

import {connectRouter, routerMiddleware} from 'connected-react-router';
import {createStore, combineReducers, applyMiddleware, compose} from 'redux';
import {createBrowserHistory} from 'history';
import {reducers as authReducers} from '../authentication/reducers';

/** The browser history */
export const history = createBrowserHistory();

/** The reducer to use for the application */
const reducer = combineReducers({
    router: connectRouter(history),
    auth: authReducers,
});

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

/** The Redux store to use */
export const store = createStore(
    reducer,
    {},
    composeEnhancers(
        applyMiddleware(
            routerMiddleware(history),
        ),
    ),
);
