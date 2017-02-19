import React from 'react'

import 'semantic-ui/dist/components/label.css'

import SimpleCodeToken from './SimpleCodeToken.js'
import './CodeSnippetWithInlineComments.css'

let commentIdx = 0

const CircleBadge = ({idx}) => {
    return <span className="ui blue circular label">{idx}</span>
}

const SpecialCommentToken = ({token}) => {
    if (token.type === 'comment') {
        commentIdx++
        return <CircleBadge idx={commentIdx}/>
    }

    return (<SimpleCodeToken token={token}/>)
}

const Bullet = ({comment, idx}) => {
    return <div><CircleBadge idx={idx}/> <span className="code-bullet-comment">{trimComment(comment)}</span></div>
}

const CodeSnippetWithInlineComments = ({tokens}) => {
    commentIdx = 0
    const comments = tokens.filter(t => t.type === 'comment');

    return <div>
        <pre><code>{tokens.map((t, idx) => <SpecialCommentToken key={idx} token={t}/>)}</code></pre>
        {comments.length ? <div className="code-bullets">{comments.map((t, idx) =>
                <Bullet key={idx} comment={t.data} idx={idx + 1}/>)}</div> : null}
    </div>
}

function trimComment(comment) {
    const trimmed = comment.trim();
    if (trimmed.startsWith("//")) {
        return trimmed.substr(2).trim()
    }

    return trimmed
}

export default CodeSnippetWithInlineComments