// @flow

import {connectRouter, routerMiddleware} from 'connected-react-router';
import {createStore, combineReducers, applyMiddleware, compose} from 'redux';
import {createBrowserHistory} from 'history';

/** The browser history */
export const history = createBrowserHistory();

/** The reducer to use for the application */
const reducer = combineReducers({
    router: connectRouter(history)
});

/** The Redux store to use */
export const store = createStore(
    reducer,
    {},
    compose(
        applyMiddleware(
            routerMiddleware(history),
        ),
    ),
);
