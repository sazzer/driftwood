import React, {Component} from 'react';
import {Interpolate} from 'react-i18next';

class App extends Component {
  render() {
    return (
      <div className="App">
        <Interpolate i18nKey="page.title" />
      </div>
    );
  }
}

export default App;
