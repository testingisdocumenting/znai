import * as React from "react"

import {isInlinedComment} from '../code-snippets/codeUtils'
import {isAllAtOnce} from '../meta/meta'
import {convertToList} from '../propsUtils';

import SnippetContainer from '../code-snippets/SnippetContainer'
import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import {parseCode} from '../code-snippets/codeParser'

import './Snippet.css'

const Snippet = (props) => {
    const tokensToUse = parseCodeWithCompatibility({lang: props.lang, snippet: props.snippet, tokens: props.tokens})

    const snippetComponent = props.commentsType === 'inline' ?
        CodeSnippetWithInlineComments :
        SimpleCodeSnippet

    return <SnippetContainer {...props} tokens={tokensToUse} snippetComponent={snippetComponent}/>
}

const presentationSnippetHandler = {
    component: Snippet,
    numberOfSlides: ({meta, commentsType, lang, snippet, tokens, highlight}) => {
        const tokensToUse = parseCodeWithCompatibility({lang, snippet, tokens})
        const highlightAsList = convertToList(highlight)

        if (commentsType === 'inline') {
            return inlinedCommentsNumberOfSlides({meta, tokens: tokensToUse})
        } else if (highlightAsList.length) {
            return highlightNumberOfSlides({meta, highlightAsList})
        } else {
            return 1
        }
    },

    slideInfoProvider: ({meta, commentsType, lang, snippet, tokens, slideIdx}) => {
        const tokensToUse = parseCodeWithCompatibility({lang, snippet, tokens})

        if (isAllAtOnce(meta)) {
            return {}
        }

        if (commentsType !== 'inline') {
            return {}
        }

        const comments = tokensToUse.filter(t => isInlinedComment(t))

        return {
            slideVisibleNote: !comments.length ? null :
                slideIdx === 0 ? "" : comments[slideIdx - 1].content
        }
    }
}

// TODO for backward compatibility with already built and deployed docs
// remove once TSI rebuilds all the docs
function parseCodeWithCompatibility({lang, tokens, snippet}) {
    if (tokens) {
        return tokens
    }

    return parseCode(lang, snippet)
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
