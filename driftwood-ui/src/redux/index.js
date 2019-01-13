// @flow

import {createStore} from 'redux-box';
import {connectRouter, routerMiddleware} from 'connected-react-router';
import {createBrowserHistory} from 'history';
import {module as dummyModule} from './dummyModule';

export const history = createBrowserHistory();

const reduxBoxConfig = {
    middlewares: [
        routerMiddleware(history)
    ],
    reducers: {
        router: connectRouter(history)
    }
};
export const store = createStore([
    dummyModule
], reduxBoxConfig);
