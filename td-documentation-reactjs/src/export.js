import Documentation from './doc-elements/Documentation'
import LunrIndexer from './doc-elements/search/LunrIndexer'
import {parseCode} from './doc-elements/code-snippets/codeParser'

import './App.css'

global.Documentation = Documentation
global.LunrIndexer = LunrIndexer
global.parseCode = parseCode