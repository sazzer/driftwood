import 'semantic-ui-css/semantic.min.css'
import i18n from './i18n';
import {I18nextProvider, translate} from 'react-i18next';
import {Provider} from 'react-redux';
import {store} from './redux';
import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';

/**
 * The contents of the app, wrapped in the translations layer
 */
const TranslatedAppContents = translate(['driftwood'], {wait: true})(App);

/**
 * The wrapper around the main application to set everything up
 * @return {*} the main application
 */
const AppWrapper = () => (
    <I18nextProvider i18n={ i18n }>
        <Provider store={store}>
            <TranslatedAppContents />
        </Provider>
    </I18nextProvider>
);

ReactDOM.render(<AppWrapper />, document.getElementById('root'));
