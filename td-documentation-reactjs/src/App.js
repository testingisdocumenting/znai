import React, {Component} from 'react'

import 'bootstrap/dist/css/bootstrap.css'
import './App.css'

import {ComponentsViewer, Registry} from 'react-components-viewer'
import {tabsDemo} from './doc-elements/tabs/TabsDemo'
import {jsonDemo} from './doc-elements/json/JsonDemo'

const snippets = new Registry('snippets')
snippets.registerAsTwoColumnTable('Json', jsonDemo)

const layout = new Registry('layout')
layout.registerAsGrid('Tabs', tabsDemo)

class App extends Component {
    render() {
        return (
            <ComponentsViewer registries={[snippets, layout]}/>
        );
    }
}

export default App;
