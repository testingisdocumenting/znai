import Documentation from './doc-elements/Documentation'
import DocumentationPreparationScreen from './screens/documentation-preparation/DocumentationPreparationScreen'
import LunrIndexer from './doc-elements/search/LunrIndexer'
import {parseCode} from './doc-elements/code-snippets/codeParser'
import {setTocJson} from './doc-elements/structure/TableOfContents'

import './App.css'

global.Documentation = Documentation
global.DocumentationPreparationScreen = DocumentationPreparationScreen
global.LunrIndexer = LunrIndexer
global.parseCode = parseCode
global.setTocJson = setTocJson