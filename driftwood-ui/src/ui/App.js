// @flow

import React from 'react';
import {Container} from 'semantic-ui-react';
import HeaderBar from './header';

/**
 * Main Application UI component
 * @param t The means to do translations
 * @return {*} The actual UI component
 * @constructor
 */
function App() {
  return (
      <Container fluid>
          <HeaderBar />
      </Container>
  );
}

export default App;
