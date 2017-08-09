import * as React from "react"

import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import {isInlinedComment} from '../code-snippets/codeUtils'
import {isAllAtOnce} from '../meta/meta'

import './Snippet.css'

class Snippet extends React.Component {
    render() {
        const {maxLineLength, commentsType} = this.props

        const CodeSnippet = commentsType === 'inline' ? CodeSnippetWithInlineComments : SimpleCodeSnippet
        const divClassName = "snippet " + (maxLineLength && maxLineLength >= 115 ? "wide-screen" : "content-block")

        return (<div className={divClassName}>
            <CodeSnippet {...this.props}/>
        </div>)
    }
}

const presentationSnippetHandler = {component: Snippet,
    numberOfSlides: ({meta, commentsType, tokens}) => {
        if (commentsType !== 'inline') {
            return 1
        }

        const comments = tokens.filter(t => isInlinedComment(t))

        if (isAllAtOnce(meta) && comments.length > 0) {
            return 2 // no highlights and all highlighted at once
        }

        return comments.length + 1
    },

    slideInfoProvider: ({meta, tokens, slideIdx}) => {
        const comments = tokens.filter(t => isInlinedComment(t))

        if (isAllAtOnce(meta)) {
            return {}
        }

        return {slideVisibleNote: !comments.length ? null :
            slideIdx === 0 ? "" : comments[slideIdx - 1].content}
    }}

export {Snippet, presentationSnippetHandler}
