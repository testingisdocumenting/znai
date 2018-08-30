import Documentation from './doc-elements/Documentation'
import DocumentationPreparationScreen from './screens/documentation-preparation/DocumentationPreparationScreen'
import Landing from './screens/landing/Landing'
import lunr from 'lunr'
import {setTocJson} from './doc-elements/structure/toc/TableOfContents'

import './App.css'

global.Documentation = Documentation
global.DocumentationPreparationScreen = DocumentationPreparationScreen
global.Landing = Landing
global.lunr = lunr
global.setTocJson = setTocJson