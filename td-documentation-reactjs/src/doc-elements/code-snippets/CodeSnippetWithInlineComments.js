import React from 'react'

import 'semantic-ui-css/components/label.css'

import SimpleCodeToken from './SimpleCodeToken'
import LineOfTokens from './LineOfTokens'
import BulletExplanations from './BulletExplanations'
import CircleBadge from './CircleBadge'

import {isAllAtOnce} from '../meta/meta'
import {containsInlinedComment, isInlinedComment, splitTokensIntoLines} from './codeUtils'

import './CodeSnippetWithInlineComments.css'

let commentIdx = 0

const SpecialCommentToken = ({token, isPresentation}) => {
    if (isInlinedComment(token)) {
        if (isPresentation) {
            return null
        } else {
            commentIdx++
            return <CircleBadge idx={commentIdx}/>
        }
    }

    return (<SimpleCodeToken token={token}/>)
}

const Explanations = ({spoiler, isPresentation, slideIdx, comments}) => {
    if (isPresentation || comments.length === 0) {
        return null
    }

    return <BulletExplanations spoiler={spoiler}
                               comments={isPresentation ? comments.slice(slideIdx, slideIdx + 1) : comments}/>
}

const CodeSnippetWithInlineComments = ({tokens, spoiler, isPresentation, meta, slideIdx}) => {
    commentIdx = 0
    const comments = tokens.filter(t => isInlinedComment(t))
    const lines = splitTokensIntoLines(tokens)

    const idxOfLinesWithComments = []
    lines.forEach((line, idx) => {
        if (containsInlinedComment(line)) {
            idxOfLinesWithComments.push(idx)
        }
    })

    // slideIdx === 0 means no highlights, 1 - first comment, etc
    const highlightIsVisible = slideIdx > 0

    const className = "code-with-inlined-comments" + (highlightIsVisible ? " with-highlighted-line" : "")

    return (
        <div className={className}>
            <pre>
                <code>
                    {lines.map((line, idx) => <LineOfTokens key={idx} tokens={line}
                                                            isHighlighted={isHighlighted(idx)}
                                                            isPresentation={isPresentation}
                                                            TokenComponent={SpecialCommentToken}/>)}
                </code>
            </pre>

            <Explanations isPresentation={isPresentation}
                          slideIdx={slideIdx}
                          spoiler={spoiler}
                          comments={comments}/>
        </div>
    )

    function isHighlighted(idx) {
        if (isAllAtOnce(meta) && highlightIsVisible) {
            return idxOfLinesWithComments.indexOf(idx) !== -1
        }

        const lineIdxToHighlight = highlightIsVisible ? idxOfLinesWithComments[slideIdx - 1] : -1
        return lineIdxToHighlight === idx
    }
}

export default CodeSnippetWithInlineComments
