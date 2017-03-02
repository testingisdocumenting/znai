import Documentation from './doc-elements/Documentation'
import LunrIndexer from './doc-elements/search/LunrIndexer'
import {parseCode} from './doc-elements/code-snippets/codeParser'
import {setTocJson} from './doc-elements/structure/TableOfContents'

import './App.css'

global.Documentation = Documentation
global.LunrIndexer = LunrIndexer
global.parseCode = parseCode
global.setTocJson = setTocJson