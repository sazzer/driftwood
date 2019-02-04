// @flow

import React from 'react';
import {Container} from 'semantic-ui-react';
import {Route} from "react-router";
import ProfilePage from './profile';
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
          <Route path='/profile' component={ProfilePage} />
      </Container>
  );
}

export default App;
