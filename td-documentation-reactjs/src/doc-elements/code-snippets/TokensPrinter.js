export class TokensPrinter {
    indentation = 0
    linesOfTokens = []

    constructor() {
        this._registerNewLine()
    }

    printDelimiter(delimiter) {
        this.print('punctuation', delimiter)
    }

    printCollapsed(delimiter, onClick) {
        this.print('collapsed', delimiter, onClick)
    }

    printCollapse(delimiter, onClick) {
        this.print('collapse', delimiter, onClick)
    }

    println() {
        this.currentLineOfTokens.push('\n')
        this._registerNewLine()
    }

    print(type, content, onClick) {
        this.currentLineOfTokens.push(onClick ? {type, content, onClick} : {type, content})
    }

    _registerNewLine() {
        this.currentLineOfTokens = []
        this.linesOfTokens.push(this.currentLineOfTokens)
    }

    printIndentation() {
        let indentation = ''
        for (let i = 0; i < this.indentation; i++) {
            indentation += '  '
        }

        this.print('indentation', indentation)
    }

    indentRight() {
        this.indentation++
    }

    indentLeft() {
        this.indentation--
    }
}
