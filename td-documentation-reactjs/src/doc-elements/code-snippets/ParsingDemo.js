import React, {Component} from 'react'
import {parseCode} from './codeParser'

import {Snippet} from '../default-elements/Snippet'

import './CodeSnippet.css'

class ParsingDemo extends Component {
    render() {
        const htmlCode = '<div id="menu">\n' +
            '  <ul>\n' +
            '   <li> <a href="/book">book</a> </li>\n' +
            '   <li> <a href="/orders">orders</a> </li>\n' +
            '   <li> <a href="/help">help</a> </li>\n' +
            ' </ul>\n' +
            '</div>\n'

        const javaCode = "class Test Test2 {\n" +
            "/*another \n" +
            " comment line \n" +
            "end of comment */\n" +
            "    var a = 2; // comment line\n" +
            "    var b = a + 1;\n" +
            "    var c = 3; //             in two lines\n" +
            "    var d = a + 1; //          another comment\n"

        const markdownCode = '# Header\n\n'+
            'Normal paragraph text. Some *italic* and **bold**.\n' +
            'Followed by bullet points:\n' +
            '* apple\n'  +
            '* banana\n' +
            '* water\n'

        const markdownCode2 = '# Server Configuration\n\n' +
            ':include-file: config/server.config\n'

        return <div>
            <Snippet title="snippet title" tokens={parseCode("html", htmlCode)}/>
            <Snippet maxLineLength={200} tokens={parseCode("html", htmlCode)}/>
            <Snippet maxLineLength={200} tokens={parseCode("html", htmlCode)}/>
            <Snippet tokens={parseCode("javascript", javaCode)} slideIdx={1} spoiler={true} commentsType="inline"/>
            <Snippet tokens={parseCode("markdown", markdownCode2)} highlight={[1]}/>
        </div>
    }
}

export default ParsingDemo