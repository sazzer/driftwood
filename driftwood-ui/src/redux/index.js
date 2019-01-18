// @flow

import {connectRouter, routerMiddleware} from 'connected-react-router';
import {createStore, combineReducers, applyMiddleware, compose} from 'redux';
import createSagaMiddleware from 'redux-saga'
import {all} from 'redux-saga/effects'
import {createBrowserHistory} from 'history';
import reducers from './reducers';
import sagas from './sagas';

/** The browser history */
export const history = createBrowserHistory();

/** The reducer to use for the application */
const reducer = combineReducers({
    router: connectRouter(history),
    ...reducers,
});

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const sagaMiddleware = createSagaMiddleware();

/** The Redux store to use */
export const store = createStore(
    reducer,
    {},
    composeEnhancers(
        applyMiddleware(
            routerMiddleware(history),
            sagaMiddleware,
        ),
    ),
);

sagaMiddleware.run(function* rootSaga() {
    yield all(sagas.map(saga => saga()));
});
