import {TokensPrinter} from '../code-snippets/TokensPrinter'

class JsonPrinter {
    printer = new TokensPrinter()

    constructor(pathsToHighlight) {
        this._pathsToHighlight = {}
        pathsToHighlight.forEach(p => this._pathsToHighlight[p] = true)
    }

    printKey(key) {
        this.printer.print('key', '"' + key + '"')
        this.printer.printDelimiter(': ')
    }

    printValue(path, value, skipIndent) {
        if (Array.isArray(value)) {
            this.printArray(path, value, skipIndent)
        } else if (typeof value === 'object') {
            this.printObject(path, value, skipIndent)
        } else {
            if (! skipIndent) {
                this.printer.printIndentation()
            }
            this.printSingleValue(path, value)
        }
    }

    printSingleValue(path, value) {
        const additionalTokenType = this.isHighlightedPath(path) ? ' highlighted' : ''

        const tokenType = typeof value === 'string' ? 'string' : 'number'
        const valueToPrint = typeof value === 'string' ? '"' + escapeQuote(value) + '"' : value

        this.printer.print(tokenType + additionalTokenType, valueToPrint)
    }

    printArray(path, values, skipIndent) {
        this.openScope('[', skipIndent)

        values.forEach((v, idx) => {
            const isLast = idx === values.length - 1

            this.printValue(path + '[' + idx + ']', v)

            if (! isLast) {
                this.printer.printDelimiter(',')
                this.printer.println()
            }
        })

        this.closeScope(']')
    }

    printObject(path, json, skipIndent) {
        this.openScope('{', skipIndent)

        const keys = Object.keys(json)
        keys.forEach((key, idx) => {
            const isLast = idx === keys.length - 1

            this.printer.printIndentation()
            this.printKey(key)
            this.printValue(path + '.' + key, json[key], true)

            if (! isLast) {
                this.printer.printDelimiter(',')
                this.printer.println()
            }
        })

        this.closeScope('}')
    }

    openScope(delimiter, skipIndent) {
        if (! skipIndent) {
            this.printer.printIndentation()
        }

        this.printer.printDelimiter(delimiter)
        this.printer.println()
        this.printer.indentRight()
    }

    closeScope(delimiter) {
        this.printer.println()
        this.printer.indentLeft()
        this.printer.printIndentation()
        this.printer.printDelimiter(delimiter)
    }

    isHighlightedPath(path) {
        return this._pathsToHighlight.hasOwnProperty(path)
    }
}

export function printJson(rootPath, data, pathsToHighlight) {
    const jsonPrinter = new JsonPrinter(pathsToHighlight || [])
    jsonPrinter.printValue(rootPath, data)

    return jsonPrinter.printer.linesOfTokens
}

function escapeQuote(text) {
    return text.replace(/"/g, "\\\"")
}
