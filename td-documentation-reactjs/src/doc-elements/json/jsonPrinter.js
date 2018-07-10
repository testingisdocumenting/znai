import {TokensPrinter} from '../code-snippets/TokensPrinter'

class JsonPrinter {
    printer = new TokensPrinter()

    constructor({pathsToHighlight, collapsedPaths, previouslyCollapsedPath, onPathUncollapse, onPathCollapse}) {
        this._pathsToHighlight = {}
        this._collapsedPaths = {}
        this._previouslyCollapsedPath = {}

        pathsToHighlight.forEach(p => this._pathsToHighlight[p] = true)
        collapsedPaths.forEach(p => this._collapsedPaths[p] = true)
        previouslyCollapsedPath.forEach(p => this._previouslyCollapsedPath[p] = true)

        this.onPathUncollapse = onPathUncollapse
        this.onPathCollapse = onPathCollapse
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
        if (values.length === 0) {
            this.printEmptyArray()
        } else if (this.isCollapsedPath(path)) {
            this.printCollapsedArray(path)
        } else {
            this.printNonEmptyArray(path, values, skipIndent)
        }
    }

    printEmptyArray() {
        this.printer.printDelimiter('[')
        this.printer.printDelimiter(']')
    }

    printCollapsedArray(path) {
        this.printer.printDelimiter('[')
        this.printCollapsed(path)
        this.printer.printDelimiter(']')
    }

    printNonEmptyArray(path, values, skipIndent) {
        const collapsable = this.isPreviouslyCollapsedPath(path) ? { path: path } : null
        this.openScope('[', skipIndent, collapsable)

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
        if (Object.keys(json).length === 0) {
            this.printEmptyObject()
        } else if (this.isCollapsedPath(path)) {
            this.printCollapsedObject(path)
        } else {
            this.printNonEmptyObject(path, json, skipIndent)
        }
    }

    printEmptyObject() {
        this.printer.printDelimiter('{')
        this.printer.printDelimiter('}')
    }

    printCollapsedObject(path) {
        this.printer.printDelimiter('{')
        this.printCollapsed(path)
        this.printer.printDelimiter('}')
    }

    printNonEmptyObject(path, json, skipIndent) {
        const collapsable = this.isPreviouslyCollapsedPath(path) ? { path: path } : null
        this.openScope('{', skipIndent, collapsable)

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

    printCollapsed(path) {
        this.printer.printCollapsed('...', () => this.onPathUncollapse(path))
    }

    printCollapse(path) {
        this.printer.printCollapse('-', () => this.onPathCollapse(path))
    }

    openScope(delimiter, skipIndent, collapsable) {
        if (! skipIndent) {
            this.printer.printIndentation()
        }

        this.printer.printDelimiter(delimiter)
        if (collapsable) {
            this.printCollapse(collapsable.path)
        }

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

    isCollapsedPath(path) {
        return this._collapsedPaths.hasOwnProperty(path)
    }

    isPreviouslyCollapsedPath(path) {
        return this._previouslyCollapsedPath.hasOwnProperty(path)
    }
}

export function printJson({rootPath, data, paths, previouslyCollapsedPath, collapsedPaths, onPathUncollapse, onPathCollapse}) {
    const jsonPrinter = new JsonPrinter({pathsToHighlight: paths || [],
        previouslyCollapsedPath: previouslyCollapsedPath || [],
        collapsedPaths: collapsedPaths || [],
        onPathUncollapse,
        onPathCollapse})

    jsonPrinter.printValue(rootPath, data)

    return jsonPrinter.printer.linesOfTokens
}

function escapeQuote(text) {
    return text.replace(/"/g, "\\\"")
}
