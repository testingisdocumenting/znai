/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import * as React from "react"

import {isInlinedComment, splitTokensIntoLines} from './codeUtils'
import {isAllAtOnce} from '../meta/meta'
import {convertToList} from '../propsUtils';

import SnippetContainer from './SnippetContainer'
import CodeSnippetWithInlineComments from './CodeSnippetWithInlineComments'
import SimpleCodeSnippet from './SimpleCodeSnippet'

import {parseCode} from './codeParser'

import './Snippet.css'

const Snippet = (props) => {
    const tokensToUse = parseCodeWithCompatibility({lang: props.lang, snippet: props.snippet, tokens: props.tokens})

    const snippetComponent = props.commentsType === 'inline' ?
        CodeSnippetWithInlineComments :
        SimpleCodeSnippet

    // TODO propagate down
    // make sure it is not being re-parsed
    const linesOfCode = splitTokensIntoLines(tokensToUse)
    return <SnippetContainer {...props}
                             tokens={tokensToUse}
                             linesOfCode={linesOfCode}
                             snippetComponent={snippetComponent}/>
}

const presentationSnippetHandler = {
    component: Snippet,
    numberOfSlides: ({meta, commentsType, lang, snippet, tokens, highlight, revealLineStop}) => {
        const tokensToUse = parseCodeWithCompatibility({lang, snippet, tokens})
        const highlightAsList = convertToList(highlight)

        if (commentsType === 'inline') {
            return inlinedCommentsNumberOfSlides({meta, tokens: tokensToUse})
        }

        return 1 + highlightNumberOfSlides({meta, highlightAsList}) + (revealLineStop || []).length;
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
        return 1
    }

    return highlightAsList.length
}

export {Snippet, presentationSnippetHandler}
