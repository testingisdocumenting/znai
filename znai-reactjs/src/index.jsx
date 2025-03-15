/*
 * Copyright 2025 znai maintainers
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

import 'normalize.css/normalize.css'
import './App.css'
import './layout/DocumentationLayout.css'
import './doc-elements/search/Search.css'

import {Documentation} from "./doc-elements/Documentation";

import {DocumentationPreparationScreen} from './screens/documentation-preparation/DocumentationPreparationScreen'
import {PreviewChangeScreen} from './screens/preview-change-path/PreviewChangeScreen'
import {NotAuthorizedScreen} from './screens/not-authorized/NotAuthorizedScreen'
import {Landing} from './screens/landing/Landing'
import {themeRegistry} from './theme/ThemeRegistry'
import {documentationNavigation} from './structure/DocumentationNavigation'
import {documentationTracking} from './doc-elements/tracking/DocumentationTracking'
import {pageTypesRegistry} from './doc-elements/page/PageTypesRegistry'
import {mergeDocMeta} from './structure/docMeta'

import lunr from 'lunr'

window.React = React
window.ReactDOM = ReactDOM
window.Documentation = Documentation
window.DocumentationPreparationScreen = DocumentationPreparationScreen
window.NotAuthorizedScreen = NotAuthorizedScreen
window.Landing = Landing
window.PreviewChangeScreen = PreviewChangeScreen
window.themeRegistry = themeRegistry
window.pageTypesRegistry = pageTypesRegistry
window.documentationNavigation = documentationNavigation
window.documentationTracking = documentationTracking
window.lunr = lunr
window.mergeDocMeta = mergeDocMeta

if (process.env.NODE_ENV !== "production") {
    import('./App').then((module) => {
        const App = module.App;
        ReactDOM.render(
          <App />,
          document.getElementById('root')
        )
    }).catch((error) => {
        console.error('Error loading the module:', error);
    });
}