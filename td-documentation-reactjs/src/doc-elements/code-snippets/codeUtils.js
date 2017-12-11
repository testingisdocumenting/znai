/**
 * splitting Prism tokens into separate lines
 * @param tokens
 * @return {Array}
 */
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
            handleNewLineStringToken(token)
        } else {
            line.push(token)
        }
    }

    function handleNewLineStringToken(token) {
        // handle multiple new line chars in a row to create empty lines
        for (let idx = 0; idx < token.length; idx++) {
            if (token.charAt(idx) === '\n') {
                line.push("\n")

                lines.push(line)
                line = []
            } else {
                line.push(token.substr(idx))
                return;
            }
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

function extractTextFromTokens(tokens) {
    return tokens.map(t => tokenToText(t)).join('')
}

function tokenToText(token) {
    if (typeof token === 'string') {
        return token
    }

    if (Array.isArray(token.content)) {
        return token.content.map(t => tokenToText(t)).join('')
    }

    return token.content.toString()
}

export {
    splitTokensIntoLines,
    isInlinedComment,
    trimComment,
    containsInlinedComment,
    extractTextFromTokens
}
