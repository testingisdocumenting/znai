/*
 * Copyright 2026 znai maintainers
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
import { Documentation} from './doc-elements/Documentation'
export { Documentation } from './doc-elements/Documentation'
import './layout/DocumentationLayout.css'
import {DocumentationPreparationScreen} from './screens/documentation-preparation/DocumentationPreparationScreen'
import {PreviewChangeScreen} from './screens/preview-change-path/PreviewChangeScreen'
import {NotAuthorizedScreen} from './screens/not-authorized/NotAuthorizedScreen'
import {DocStatsScreen} from './screens/doc-stats/DocStatsScreen'
import {Landing} from './screens/landing/Landing'
import {themeRegistry} from './theme/ThemeRegistry'
import {documentationNavigation} from './structure/DocumentationNavigation.jsx'
import {documentationTracking} from './doc-elements/tracking/DocumentationTracking'
import {pageTypesRegistry} from './doc-elements/page/PageTypesRegistry'
import {mergeDocMeta} from './structure/docMeta'
export { elementsLibrary } from './doc-elements/DefaultElementsLibrary'
export { themeRegistry } from './theme/ThemeRegistry'
export { documentationNavigation } from './structure/DocumentationNavigation'
import {createLocalSearchIndex, populateLocalSearchIndexWithData} from "./doc-elements/search/flexSearch.ts";
// Initialize global variables
function initializeGlobals() {
    if (typeof window !== 'undefined') {
        window.createLocalSearchIndex = createLocalSearchIndex;
        window.populateLocalSearchIndexWithData = populateLocalSearchIndexWithData
        window.React = React
        window.ReactDOM = ReactDOM
        window.Documentation = Documentation;
        window.DocumentationPreparationScreen = DocumentationPreparationScreen
        window.NotAuthorizedScreen = NotAuthorizedScreen
        window.DocStatsScreen = DocStatsScreen
        window.Landing = Landing
        window.PreviewChangeScreen = PreviewChangeScreen
        window.themeRegistry = themeRegistry
        window.pageTypesRegistry = pageTypesRegistry
        window.documentationNavigation = documentationNavigation
        window.documentationTracking = documentationTracking
        window.mergeDocMeta = mergeDocMeta
        window.znaiSearchIdx = window.createLocalSearchIndex();
    }
}
// Auto-initialize when loaded
initializeGlobals();

export default {initializeGlobals}