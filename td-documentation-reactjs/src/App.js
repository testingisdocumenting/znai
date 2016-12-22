import React, { Component } from 'react';

import TocPanel from './TocPanel';

import './App.css';
import 'bootstrap/dist/css/bootstrap.css'

class App extends Component {
  render() {
    return (
      <div className="container">
        <div className="row">
          <TocPanel collapsed={false}/>
        </div>
      </div>
    );
  }
}

export default App;
