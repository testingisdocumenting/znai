/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import ReactDOM from 'react-dom';

require('normalize.css/normalize.css')
require('./App.css')
require('./layout/DocumentationLayout.css')
require('./doc-elements/search/Search.css')

const {Documentation} = require('./doc-elements/Documentation')
const {DocumentationPreparationScreen} = require('./screens/documentation-preparation/DocumentationPreparationScreen')
const {Landing} = require('./screens/landing/Landing')
const {themeRegistry} = require('./theme/ThemeRegistry')
const {documentationNavigation} = require('./structure/DocumentationNavigation')
const {documentationTracking} = require('./doc-elements/tracking/DocumentationTracking')
const {pageTypesRegistry} = require('./doc-elements/page/PageTypesRegistry')
const {mergeDocMeta} = require('./doc-elements/docMeta')

const lunr = require('lunr')

global.React = React
global.ReactDOM = ReactDOM
global.Documentation = Documentation
global.DocumentationPreparationScreen = DocumentationPreparationScreen
global.Landing = Landing
global.themeRegistry = themeRegistry
global.pageTypesRegistry = pageTypesRegistry
global.documentationNavigation = documentationNavigation
global.documentationTracking = documentationTracking
global.lunr = lunr
global.mergeDocMeta = mergeDocMeta

if (process.env.NODE_ENV !== "production") {
    const App = require('./App').App

    ReactDOM.render(
        <App />,
        document.getElementById('root')
    )
}