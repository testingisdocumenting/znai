import React, {Component} from 'react'
import {parseCode} from './codeParser'

import CodeSnippetWithInlineComments from './CodeSnippetWithInlineComments'

class ParsingDemo extends Component {
    render() {
        const htmlCode = '<div id="menu">\n' +
            '<ul>\n' +
            '  <li> <a href="/book">book</a> </li>\n' +
            '  <li> <a href="/orders">orders</a> </li>\n' +
            '  <li> <a href="/help">help</a> </li>\n' +
            '</ul>\n' +
            '</div>\n'

        const javaCode = "class Test Test2 {\n" +
            "    var a  = 2; // comment line\n" +
            "    var b = a + 1;// another comment\n" +
            "    var c = 3;// in two lines\n" +
            "    var d = a + 1;\n"

        const markdownCode = '# Header\n\n'+
            'Normal paragraph text. Some *italic* and **bold**.\n' +
            'Followed by bullet points:\n' +
            '* apple\n'  +
            '* banana\n' +
            '* water\n'

        const markdownCode2 = '# Server Configuration\n\n' +
            ':include-file: config/server.config\n'

        return <div>
            <CodeSnippetWithInlineComments tokens={parseCode("html", htmlCode)}/>
            <CodeSnippetWithInlineComments tokens={parseCode("javascript", javaCode)}/>
            <CodeSnippetWithInlineComments tokens={parseCode("markdown", markdownCode2)}/>
        </div>
    }
}

export default ParsingDemo