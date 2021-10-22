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

/**
 * splitting Prism tokens into separate lines
 * @param tokens
 * @return {Array}
 */
export function splitTokensIntoLines(tokens) {
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
        } else if (isString && token.startsWith(' ')) {
            handleSpacing(token)
        } else {
            pushToken(token)
        }
    }

    function handleMultiLineStringToken(token) {
        const parts = token.split('\n')

        for (let idx = 0; idx < parts.length; idx++) {
            const isLastPart = (idx === parts.length - 1)

            handleSpacing(parts[idx])

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
                lines.push(line)
                line = []
            } else {
                handleSpacing(token.substr(idx))
                return;
            }
        }
    }

    function handleSpacing(token) {
        const nonSpaceIdx = findNonSpaceIdx()

        if (nonSpaceIdx === token.length || nonSpaceIdx === 0) {
            pushToken(token)
        } else {
            pushToken(token.substr(0, nonSpaceIdx))
            if (nonSpaceIdx > 0) {
                pushToken(token.substr(nonSpaceIdx))
            }
        }

        function findNonSpaceIdx() {
            for (let idx = 0; idx < token.length; idx++) {
                if (token.charAt(idx) !== ' ') {
                    return idx
                }
            }

            return token.length
        }
    }

    function pushToken(token) {
        if (token) {
            line.push(token)
        }
    }
}

export function isCommentToken(token) {
    return token.type === 'comment'
}

export function trimComment(comment) {
    const trimmed = comment.trim()
    return trimmed.substr(commentWidth(trimmed)).trim()
}

export function findComments(lines) {
    const result = []
    lines.forEach(line => {
        line.forEach(token => {
            if (isCommentToken(token)) {
                result.push(token)
            }
        })
    })

    return result
}

function commentWidth(comment) {
    if (comment.startsWith('//')) {
        return 2;
    }

    if (comment.startsWith('#')) {
        return 1;
    }

    return 0;
}

export function containsInlinedComment(tokens) {
    return tokens.filter(t => isCommentToken(t)).length
}

export function extractTextFromTokens(tokens) {
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

export function isSimpleValueToken(token) {
    return typeof token === 'string' || typeof token === 'number'
}

export function collapseCommentsAboveToMakeCommentOnTheCodeLine(lines) {
    const newLines = []
    let accumulatedCommentLines = []

    for (let idx = 0; idx < lines.length; idx++) {
        const line = lines[idx]
        const comment = extractCommentIfCommentOnlyLine(line)
        if (comment) {
            accumulatedCommentLines.push(comment)
        } else {
            const combinedComment = accumulatedCommentLines.join(' ')
            newLines.push(
                combinedComment.length > 0 ?
                    [...lineWithTokensTrimmedOnRight(line), {type: 'comment', content: combinedComment}] :
                    line)

            accumulatedCommentLines = []
        }
    }

    return newLines
}

export function removeCommentsFromEachLine(lines) {
    const newLines = []

    for (let idx = 0; idx < lines.length; idx++) {
        const line = lines[idx]
        const comment = extractCommentIfCommentOnlyLine(line)
        if (!comment) {
            newLines.push(removeCommentTokens(line))
        }
    }

    return newLines
}

function removeCommentTokens(line) {
    return lineWithTokensTrimmedOnRight(
      line.filter(token => !isCommentToken(token)))
}

export function lineWithTokensTrimmedOnRight(line) {
    const endIdx = findEndIdx()
    if (endIdx === line.length - 1) {
        return line
    }

    return line.slice(0, endIdx + 1)

    function findEndIdx() {
        let endIdx = line.length - 1
        for (; endIdx >= 0; endIdx--) {
            if (tokenToText(line[endIdx]).trim().length > 0) {
                return endIdx
            }
        }

        return 0
    }
}

function extractCommentIfCommentOnlyLine(line) {
    for (let idx = 0; idx < line.length; idx++) {
        const token = line[idx]

        if (tokenToText(token).trim().length === 0) {
            continue
        }

        if (isCommentToken(token)) {
            return trimComment(token.content)
        }

        return undefined
    }

    return undefined
}

export function enhanceMatchedTokensWithMeta(tokens, expressions, extraTypeProvider, linkProvider) {
    const enhancedTokens = [...tokens]

    const matches = findTokensThatMatchExpressions(tokens, expressions)
    Object.keys(matches).forEach(updateTokensForMatch)

    return enhancedTokens

    function updateTokensForMatch(expression) {
        const match = matches[expression]
        for (let idx = match[0]; idx <= match[1]; idx++) {
            enhancedTokens[idx] = updateWithNewMeta(tokens[idx], expression)
        }
    }

    function updateWithNewMeta(token, expression) {
        const extraType = extraTypeProvider(expression)
        const existingType = isSimpleValueToken(token) ? 'text' : token.type

        const enhancement = {
            link: linkProvider(expression),
            type: existingType + (extraType.length > 0 ? ' ' : '') + extraType
        }

        if (isSimpleValueToken(token)) {
            return {content: token, ...enhancement}
        }

        return {...token, ...enhancement}
    }
}

export function findTokensThatMatchExpressions(tokens, expressions) {
    const extractedTexts = tokens.map(t => tokenToText(t).trim())

    const result = {}
    expressions.forEach(expression => {
        const indexes = findIndexes(expression)
        if (!indexes) {
            return
        }

        result[expression] = indexes
    })

    return result

    function findIndexes(expression) {
        let running = ''
        let startIdx = 0

        for (let idx = 0; idx < extractedTexts.length; idx++) {
            const tokenText = extractedTexts[idx]

            if (tokenText.length === 0) {
                if (running.length === 0) {
                    startIdx = idx + 1
                }

                continue
            }

            running += tokenText

            if (expression.indexOf(running) !== 0) {
                running = ''
                startIdx = idx + 1
            } else {
                if (running.length === expression.length) {
                    return [startIdx, idx]
                }
            }
        }

        return undefined
    }
}
