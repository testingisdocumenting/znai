import React from 'react'

import 'semantic-ui/dist/components/label.css'

import SimpleCodeToken from './SimpleCodeToken.js'
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

const LineOfTokens = ({line, isHighlighted, isPresentation}) => {
    const className = "code-line" + (isHighlighted ? " highlight" : "")

    return <span className={className}>
        {line.map((t, idx) => <SpecialCommentToken key={idx} token={t} isPresentation={isPresentation}/>)}
    </span>
}

const BulletExplanations = ({comments}) => <div className="code-bullets">
    {comments.map((t, idx) => <Bullet key={idx} comment={t.content} idx={idx + 1}/>)}</div>

const Explanations = ({isPresentation, slideIdx, comments}) => {
    if (! isPresentation) {
        return <BulletExplanations comments={isPresentation ? comments.slice(slideIdx, slideIdx + 1) : comments}/>
    }

    const hiddenComment = slideIdx === 0
    const comment = hiddenComment ? " " : comments[slideIdx - 1].content
    const className = "presentation-code-comment" + (hiddenComment ? "" : " divider")

    return <div className={className}>{trimComment(comment)}</div>
}

const CodeSnippetWithInlineComments = ({tokens, slideIdx}) => {
    commentIdx = 0
    const comments = tokens.filter(t => isInlinedComment(t))
    const lines = splitTokensIntoLines(tokens)

    const idxOfLinesWithComments = []
    lines.forEach((line, idx) => {
        if (containsInlinedComment(line)) {
            idxOfLinesWithComments.push(idx)
        }
    })

    const isPresentation = typeof slideIdx !== 'undefined'

    // slideIdx === 0 means no highlights, 1 - first comment, etc
    const noHighlights = slideIdx === 0
    const lineIdxToHighlight = noHighlights ? -1 : idxOfLinesWithComments[slideIdx - 1]

    const className = "code-with-inlined-comments" + (noHighlights ? "" : " with-highlighted-line")

    return <div className={className}>
        <pre>
            <code>
                {lines.map((line, idx) => <LineOfTokens key={idx} line={line}
                                                        isHighlighted={lineIdxToHighlight === idx}
                                                        isPresentation={isPresentation}/>)}
            </code>
        </pre>

        <Explanations isPresentation={isPresentation} slideIdx={slideIdx} comments={comments}/>
    </div>
}

export default CodeSnippetWithInlineComments