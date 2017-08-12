import React from 'react'

import 'semantic-ui-css/components/label.css'

import SimpleCodeToken from './SimpleCodeToken.js'
import LineOfTokens from './LineOfTokens'
import {isAllAtOnce} from '../meta/meta'
import {splitTokensIntoLines, isInlinedComment, trimComment, containsInlinedComment} from './codeUtils'

import './CodeSnippetWithInlineComments.css'

let commentIdx = 0

const CircleBadge = ({idx}) => {
    return <span className="ui blue circular label">{idx}</span>
}

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

const Bullet = ({comment, idx}) => {
    return <div><CircleBadge idx={idx}/> <span className="code-bullet-comment">{trimComment(comment)}</span></div>
}

const BulletExplanations = ({comments}) => <div className="code-bullets">
    {comments.map((t, idx) => <Bullet key={idx} comment={t.content} idx={idx + 1}/>)}</div>

const Explanations = ({isPresentation, slideIdx, comments}) => {
    if (!isPresentation) {
        return <BulletExplanations comments={isPresentation ? comments.slice(slideIdx, slideIdx + 1) : comments}/>
    }

    return null
}

const CodeSnippetWithInlineComments = ({tokens, isPresentation, meta, slideIdx}) => {
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
                    {lines.map((line, idx) => <LineOfTokens key={idx} line={line}
                                                            isHighlighted={isHighlighted(idx)}
                                                            isPresentation={isPresentation}
                                                            TokenComponent={SpecialCommentToken}/>)}
                </code>
            </pre>

            <Explanations isPresentation={isPresentation} slideIdx={slideIdx} comments={comments}/>
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
