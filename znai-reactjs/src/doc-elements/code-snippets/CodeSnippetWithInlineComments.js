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

import React from 'react'

import LineOfTokens from './LineOfTokens'
import { SnippetCircleBadge } from './explanations/SnippetCircleBadge'

import {isAllAtOnce} from '../meta/meta'
import {
    collapseCommentsAboveToMakeCommentOnTheCodeLine,
    containsInlinedComment,
    isCommentToken,
    lineWithTokensTrimmedOnRight,
    splitTokensIntoLines, trimComment
} from "./codeUtils";

import {mergeWithGlobalDocReferences} from '../references/globalDocReferences'

import './CodeSnippetWithInlineComments.css'

const CodeSnippetWithInlineComments = ({tokens, references, comments, isPresentation, meta, slideIdx, wrap}) => {
    const lines = collapseCommentsAboveToMakeCommentOnTheCodeLine(splitTokensIntoLines(tokens))

    const idxOfLinesWithComments = []
    const bulletNumbersPerLineIdx = []
    let bulletNumber = 1

    lines.forEach((line, idx) => {
        if (containsInlinedComment(line)) {
            idxOfLinesWithComments.push(idx)
            bulletNumbersPerLineIdx[idx] = bulletNumber++
        }
    })

    // slideIdx === 0 means no highlights, 1 - first comment, etc
    const highlightIsVisible = slideIdx > 0

    const className = "code-with-inlined-comments" + (highlightIsVisible ? " with-highlighted-line" : "")
    const mergedReferences = mergeWithGlobalDocReferences(references)

    return (
        <div className={className}>
            <pre>
                {lines.map((line, idx) => {
                    const bulletIdxForLine = bulletNumbersPerLineIdx[idx]
                    const lineToRender = bulletIdxForLine ?
                        removeCommentAtTheEnd(line) :
                        line

                    return <LineOfTokens key={idx}
                                         tokens={lineToRender}
                                         isHighlighted={isHighlighted(idx)}
                                         references={mergedReferences}
                                         isPresentation={isPresentation}
                                         wrap={wrap}
                                         endOfLineRender={() => {
                                             const bulletNumberForLine = bulletNumbersPerLineIdx[idx]
                                             return bulletNumberForLine ?
                                               <SnippetCircleBadge className="left-margin"
                                                                   idx={bulletNumberForLine}
                                                                   tooltip={comments[bulletNumberForLine - 1] ?
                                                                     trimComment(comments[bulletNumberForLine - 1].content): ""} /> :
                                               null
                                         }}
                    />
                })}
            </pre>
        </div>
    )

    function removeCommentAtTheEnd(line) {
        const trimmed = lineWithTokensTrimmedOnRight(line)
        const lastToken = trimmed[trimmed.length - 1]
        if (isCommentToken(lastToken)) {
            trimmed.splice(trimmed.length - 1, 1)
        }

        return trimmed
    }

    function isHighlighted(idx) {
        if (isAllAtOnce(meta) && highlightIsVisible) {
            return idxOfLinesWithComments.indexOf(idx) !== -1
        }

        const lineIdxToHighlight = highlightIsVisible ? idxOfLinesWithComments[slideIdx - 1] : -1
        return lineIdxToHighlight === idx
    }
}

export default CodeSnippetWithInlineComments
