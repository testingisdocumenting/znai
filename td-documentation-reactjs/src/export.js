import './App.css'

import {Documentation} from './doc-elements/Documentation'
import {themeRegistry} from './theme/ThemeRegistry'

import {DocumentationPreparationScreen} from './screens/documentation-preparation/DocumentationPreparationScreen'
import {Landing} from './screens/landing/Landing'

import * as lunr from 'lunr'

export {
    Documentation,
    DocumentationPreparationScreen,
    Landing,
    lunr,
    themeRegistry
}

// global.React = React
// global.Documentation = Documentation
// global.DocumentationPreparationScreen = DocumentationPreparationScreen
// global.Landing = Landing
// global.lunr = lunr
//
// export function renderDoc() {
//     return 2 + 2
// }
