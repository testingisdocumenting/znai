/*
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
        const isString = typeof token === 'string'

        if (isString && token.indexOf('\n') > 0) {
            handleMultiLineStringToken(token)
        } else if (isString && token.startsWith('\n')) {
            handleNewLineStringToken(token)
        } else {
            line.push(token)
        }
    }

    function handleMultiLineStringToken(token) {
        const parts = token.split('\n')

        for (let idx = 0; idx < parts.length; idx++) {
            const isLastPart = (idx === parts.length - 1)

            line.push(parts[idx] + (isLastPart ? '' : '\n'))

            if (!isLastPart) {
                lines.push(line)
                line = []
            }
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
