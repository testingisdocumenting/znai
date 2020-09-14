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

import {
    collapseCommentsAboveToMakeCommentOnTheCodeLine,
    findComments,
    isCommentToken,
    splitTokensIntoLines
} from './codeUtils'
import {isAllAtOnce} from '../meta/meta'
import {convertToList} from '../propsUtils';

import SnippetContainer from './SnippetContainer'
import CodeSnippetWithInlineComments from './CodeSnippetWithInlineComments'
import SimpleCodeSnippet from './SimpleCodeSnippet'

import {parseCode} from './codeParser'
import {countNumberOfLines} from "../../utils/strings";

import BulletExplanations from './explanations/BulletExplanations';

import './Snippet.css'

const defaultNumberOfVisibleLines = 25

const bulletCommentsType = 'inline'

const Snippet = (props) => {
    const tokensToUse = parseCodeWithCompatibility({lang: props.lang, snippet: props.snippet, tokens: props.tokens})

    const renderBulletComments = props.commentsType === bulletCommentsType;

    const snippetComponent = renderBulletComments ?
        CodeSnippetWithInlineComments :
        SimpleCodeSnippet

    const linesOfCode = renderBulletComments ?
        collapseCommentsAboveToMakeCommentOnTheCodeLine(splitTokensIntoLines(tokensToUse)):
        splitTokensIntoLines(tokensToUse)

    const comments = findComments(linesOfCode)

    return (
        <>
            <SnippetContainer {...props}
                              tokens={tokensToUse}
                              linesOfCode={linesOfCode}
                              scrollToLineIdx={scrollToLineIdx(props)}
                              snippetComponent={snippetComponent}/>
            <Explanations comments={comments} {...props}/>
        </>
    )
}

Snippet.defaultProps = {
    numberOfVisibleLines: defaultNumberOfVisibleLines
}

function Explanations({commentsType, spoiler, isPresentation, slideIdx, comments}) {
    if (commentsType !== bulletCommentsType || isPresentation || comments.length === 0) {
        return null
    }

    return <BulletExplanations spoiler={spoiler}
                               comments={isPresentation ? comments.slice(slideIdx, slideIdx + 1) : comments}/>
}


function scrollToLineIdx({isPresentation, slideIdx, numberOfVisibleLines}) {
    if (!isPresentation || !numberOfVisibleLines) {
        return undefined
    }

    return numberOfVisibleLines * slideIdx
}

const presentationSnippetHandler = {
    component: Snippet,
    numberOfSlides: ({
                         meta,
                         commentsType,
                         lang,
                         snippet,
                         tokens,
                         highlight,
                         revealLineStop,
                         numberOfVisibleLines = defaultNumberOfVisibleLines
                     }) => {
        const tokensToUse = parseCodeWithCompatibility({lang, snippet, tokens})
        const highlightAsList = convertToList(highlight)

        if (commentsType === 'inline') {
            return inlinedCommentsNumberOfSlides({meta, tokens: tokensToUse})
        }

        const numberOfStopLines = (revealLineStop || []).length
        const numberOfScrolls = countNumberOfScrolls()

        const hasFirstNoActionSlide = highlightAsList.length > 0 || numberOfStopLines > 0 ||
            (highlightAsList.length === 0 && numberOfStopLines === 0 && numberOfScrolls === 0)

        return (hasFirstNoActionSlide ? 1 : 0) +
            highlightNumberOfSlides({meta, highlightAsList}) +
            numberOfStopLines +
            numberOfScrolls

        function countNumberOfScrolls() {
            const numberOfLines = countNumberOfLines(snippet)

            if (numberOfLines <= numberOfVisibleLines) {
                return 0
            }

            return Math.ceil(numberOfLines / numberOfVisibleLines)
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

        const comments = tokensToUse.filter(t => isCommentToken(t))

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
    const comments = tokens.filter(t => isCommentToken(t))

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
