import React, {Component} from 'react'
import {parseCode} from './codeParser'

import CodeSnippetWithInlineComments from './CodeSnippetWithInlineComments'

class ParsingDemo extends Component {
    render() {
        const code = "class Test Test2 {\n" +
            "    var a  = 2; // comment line\n" +
            "    var b = a + 1;// another comment\n" +
            "    var c = 3;// in two lines\n" +
            "    var d = a + 1;\n";

        const tokens = parseCode(code)
        return <div><CodeSnippetWithInlineComments tokens={tokens}/></div>
    }
}

export default ParsingDemo