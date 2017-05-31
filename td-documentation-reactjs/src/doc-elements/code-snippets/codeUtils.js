function splitTokensIntoLines(tokens) {
    const lines = []
    let line = []

    const len = tokens.length
    for (let i = 0; i < len; i++) {
        const token = tokens[i]
        handle(token)
    }

    if (line.length) {
        lines.push(line)
    }

    return lines

    function handle(token) {
        if (typeof token === 'string' && token.startsWith('\n')) {
            line.push("\n")

            lines.push(line)
            line = []

            // handle "\n    " cases
            if (token.length > 1) {
                line.push(token.substr(1))
            }
        } else {
            line.push(token)
        }
    }
}

function isInlinedComment(token) {
    return token.type === 'comment' && token.content.startsWith("//")
}

function trimComment(comment) {
    const trimmed = comment.trim()
    return trimmed.substr(2).trim()
}

function containsInlinedComment(tokens) {
    return tokens.filter(t => isInlinedComment(t)).length
}

export {splitTokensIntoLines, isInlinedComment, trimComment, containsInlinedComment}
