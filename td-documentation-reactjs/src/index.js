import React from 'react';

if (process.env.NODE_ENV !== "production") {
    require('bootstrap/dist/css/bootstrap.css')
}

require('./App.css')
require('./doc-elements/DocumentationLayout.css')
require('./doc-elements/search/Search.css')

const {Documentation} = require('./doc-elements/Documentation')
const {DocumentationPreparationScreen} = require('./screens/documentation-preparation/DocumentationPreparationScreen')
const {Landing} = require('./screens/landing/Landing')
const {themeRegistry} = require('./theme/ThemeRegistry')
const lunr = require('lunr')

global.React = React
global.Documentation = Documentation
global.DocumentationPreparationScreen = DocumentationPreparationScreen
global.Landing = Landing
global.themeRegistry = themeRegistry
global.lunr = lunr

if (process.env.NODE_ENV !== "production") {
    const ReactDOM = require('react-dom');
    const App = require('./App').App

    ReactDOM.render(
        <App />,
        document.getElementById('root')
    )
}
