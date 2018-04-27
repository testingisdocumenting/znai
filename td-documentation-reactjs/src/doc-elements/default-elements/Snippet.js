import * as React from "react"

import {isInlinedComment} from '../code-snippets/codeUtils'
import {isAllAtOnce} from '../meta/meta'
import {convertToList} from '../propsUtils';

import SnippetContainer from '../code-snippets/SnippetContainer'
import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import './Snippet.css'

const Snippet = (props) => {
    const snippetComponent = props.commentsType === 'inline' ?
        CodeSnippetWithInlineComments :
        SimpleCodeSnippet

    return <SnippetContainer {...props} snippetComponent={snippetComponent}/>
}

const presentationSnippetHandler = {
    component: Snippet,
    numberOfSlides: ({meta, commentsType, tokens, highlight}) => {
        const highlightAsList = convertToList(highlight)

        if (commentsType === 'inline') {
            return inlinedCommentsNumberOfSlides({meta, tokens})
        } else if (highlightAsList.length) {
            return highlightNumberOfSlides({meta, highlightAsList})
        } else {
            return 1
        }
    },

    slideInfoProvider: ({meta, commentsType, tokens, slideIdx}) => {
        if (isAllAtOnce(meta)) {
            return {}
        }

        if (commentsType !== 'inline') {
            return {}
        }

        const comments = tokens.filter(t => isInlinedComment(t))

        return {
            slideVisibleNote: !comments.length ? null :
                slideIdx === 0 ? "" : comments[slideIdx - 1].content
        }
    }
}

function inlinedCommentsNumberOfSlides({meta, tokens}) {
    const comments = tokens.filter(t => isInlinedComment(t))

    if (isAllAtOnce(meta) && comments.length > 0) {
        return 2 // two slides: 1st - no highlights; 2nd - all highlighted at once
    }

    return comments.length + 1
}

function highlightNumberOfSlides({meta, highlightAsList}) {
    if (isAllAtOnce(meta) && highlightAsList.length > 0) {
        return 2 // two slides: 1st - no highlights; 2nd - all highlighted at once
    }

    return highlightAsList.length + 1
}

export {Snippet, presentationSnippetHandler}
