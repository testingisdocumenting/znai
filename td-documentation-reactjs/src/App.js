import React, { Component } from 'react';

import './App.css';
import 'bootstrap/dist/css/bootstrap.css'

import TestMarkDownRenderedPage from './doc-elements/VisualManualTest'

class App extends Component {
  render() {
    return (
      <div className="container">
        <div className="row">
            <TestMarkDownRenderedPage/>
        </div>
      </div>
    );
  }
}

export default App;
