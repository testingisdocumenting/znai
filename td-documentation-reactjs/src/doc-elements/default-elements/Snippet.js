import * as React from "react"

import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import {isInlinedComment} from '../code-snippets/codeUtils'

import './Snippet.css'

class Snippet extends React.Component {
    render() {
        const {tokens, maxLineLength, commentsType} = this.props

        const CodeSnippet = commentsType === 'inline' ? CodeSnippetWithInlineComments : SimpleCodeSnippet
        const divClassName = "snippet " + (maxLineLength && maxLineLength >= 89 ? "wide-screen" : "content-block")

        return (<div className={divClassName}>
            <CodeSnippet {...this.props}/>
        </div>)
    }
}

const presentationSnippetHandler = {component: Snippet,
    numberOfSlides: ({commentsType, tokens}) => {
        if (commentsType !== 'inline') {
            return 1
        }

        const comments = tokens.filter(t => isInlinedComment(t))
        return comments.length + 1
    }}

export {Snippet, presentationSnippetHandler}
