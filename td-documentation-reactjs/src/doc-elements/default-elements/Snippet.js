import * as React from "react"

import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'
import {parseCode} from '../code-snippets/codeParser'

import './Snippet.css'

class Snippet extends React.Component {
    render() {
        const {snippet, lang, maxLineLength, commentsType} = this.props

        const CodeSnippet = commentsType === 'inline' ? CodeSnippetWithInlineComments : SimpleCodeSnippet
        const divClassName = "snippet " + (maxLineLength && maxLineLength > 90 ? "wide-screen" : "content-block")

        return (<div className={divClassName}>
            {lang ? <CodeSnippet tokens={parseCode(lang, snippet)}/> : <pre><code>{snippet}</code></pre>}
        </div>)
    }
}

export default Snippet;
