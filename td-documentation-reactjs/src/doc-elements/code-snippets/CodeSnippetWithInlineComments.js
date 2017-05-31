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
    const lineIdxToHighlight = slideIdx === 0 ? -1 : idxOfLinesWithComments[slideIdx - 1]

    return <div>
        <pre>
            <code>
                {lines.map((line, idx) => <LineOfTokens key={idx} line={line}
                                                        isHighlighted={lineIdxToHighlight === idx}
                                                        isPresentation={isPresentation}/>)}
            </code>
        </pre>

        {comments.length && ! isPresentation ?
            <BulletExplanations comments={comments}/> : null}
    </div>
}

export default CodeSnippetWithInlineComments