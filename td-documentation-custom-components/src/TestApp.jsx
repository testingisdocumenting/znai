import React from 'react';
import {render} from 'react-dom';

import './App.css';

class App extends React.Component {
    render () {
        return <p> Hello React project</p>;
    }
}

render(<App/>, document.getElementById('app'));