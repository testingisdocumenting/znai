import React, {Component} from 'react'

import 'bootstrap/dist/css/bootstrap.css'
import './App.css'

import {ComponentsViewer, Registry} from 'react-components-viewer'
import {tabsDemo} from './doc-elements/tabs/Tabs.demo'
import {jsonDemo} from './doc-elements/json/Json.demo'
import {langClassDemo} from './doc-elements/lang/LangClass.demo'
import {langFunctionDemo} from './doc-elements/lang/LangFunction.demo'
import {documentationDemo} from './doc-elements/Documentation.demo'

const snippets = new Registry('snippets')
snippets.registerAsGrid('Json', 0, jsonDemo)
snippets.registerAsGrid('Lang Class', 0, langClassDemo)
snippets.registerAsGrid('Lang Function', 0, langFunctionDemo)

const layout = new Registry('layout')
layout.registerAsGrid('Tabs', 300, tabsDemo)

const endToEnd = new Registry('end to end')
endToEnd.registerSingle('full documentation navigation', documentationDemo)

class App extends Component {
    render() {
        return (
            <ComponentsViewer registries={[snippets, layout, endToEnd]}/>
        );
    }
}

export default App;
