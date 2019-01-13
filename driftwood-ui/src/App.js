import React from 'react';
import {withI18n} from 'react-i18next';

function App({t}) {
  return (
      <div className="App">
        {t('page.title')}
      </div>
  );
}

export default withI18n()(App);
