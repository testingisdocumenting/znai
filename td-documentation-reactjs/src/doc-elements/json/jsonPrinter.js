export function printJson(rootPath, data, pathsToHighlight) {
    const printer = new JsonPrinter(pathsToHighlight || [])
    printer.printValue(rootPath, data)

    return printer.linesOfTokens
}

class JsonPrinter {
    indentation = 0
    linesOfTokens = []

    constructor(pathsToHighlight) {
        this._pathsToHighlight = {}
        pathsToHighlight.forEach(p => this._pathsToHighlight[p] = true)

        this._registerNewLine()
    }

    printKey(key) {
        this.print('key', '"' + key + '"')
        this.printDelimiter(': ')
    }

    printDelimiter(delimiter) {
        this.print('punctuation', delimiter)
    }

    println() {
        this.currentLineOfTokens.push('\n')
        this._registerNewLine()
    }

    print(type, content) {
        this.currentLineOfTokens.push({type, content})
    }

    _registerNewLine() {
        this.currentLineOfTokens = []
        this.linesOfTokens.push(this.currentLineOfTokens)
    }

    printValue(path, value, skipIndent) {
        if (Array.isArray(value)) {
            this.printArray(path, value, skipIndent)
        } else if (typeof value === 'object') {
            this.printObject(path, value, skipIndent)
        } else {
            if (! skipIndent) {
                this.printIndentation()
            }
            this.printSingleValue(path, value)
        }
    }

    printIndentation() {
        let indentation = ''
        for (let i = 0; i < this.indentation; i++) {
            indentation += '  '
        }

        this.print('indentation', indentation)
    }

    printSingleValue(path, value) {
        const additionalTokenType = this.isHighlightedPath(path) ? ' highlighted' : ''

        const tokenType = typeof value === 'string' ? 'string' : 'number'
        const valueToPrint = typeof value === 'string' ? '"' + escapeQuote(value) + '"' : value

        this.print(tokenType + additionalTokenType, valueToPrint)
    }

    printArray(path, values, skipIndent) {
        this.openScope('[', skipIndent)

        values.forEach((v, idx) => {
            const isLast = idx === values.length - 1

            this.printValue(path + '[' + idx + ']', v)

            if (! isLast) {
                this.printDelimiter(',')
                this.println()
            }
        })

        this.closeScope(']')
    }

    printObject(path, json, skipIndent) {
        this.openScope('{', skipIndent)

        const keys = Object.keys(json)
        keys.forEach((key, idx) => {
            const isLast = idx === keys.length - 1

            this.printIndentation()
            this.printKey(key)
            this.printValue(path + '.' + key, json[key], true)

            if (! isLast) {
                this.printDelimiter(',')
                this.println()
            }
        })

        this.closeScope('}')
    }

    openScope(delimiter, skipIndent) {
        if (! skipIndent) {
            this.printIndentation()
        }

        this.printDelimiter(delimiter)
        this.println()
        this.indentRight()
    }

    closeScope(delimiter) {
        this.println()
        this.indentLeft()
        this.printIndentation()
        this.printDelimiter(delimiter)
    }

    indentRight() {
        this.indentation++
    }

    indentLeft() {
        this.indentation--
    }

    isHighlightedPath(path) {
        return this._pathsToHighlight.hasOwnProperty(path)
    }
}

function escapeQuote(text) {
    return text.replace(/"/g, "\\\"")
}
