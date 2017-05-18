import * as React from "react"

import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'
import './Snippet.css'

class Snippet extends React.Component {
    render() {
        const {tokens, maxLineLength, commentsType} = this.props

        const CodeSnippet = commentsType === 'inline' ? CodeSnippetWithInlineComments : SimpleCodeSnippet
        const divClassName = "snippet " + (maxLineLength && maxLineLength >= 87 ? "wide-screen" : "content-block")

        return (<div className={divClassName}>
            <CodeSnippet tokens={tokens}/>
        </div>)
    }
}

const presentationSnippetHandler = {component: Snippet,
    numberOfSlides: () => 1}

export {Snippet, presentationSnippetHandler}
