export class TokensPrinter {
    indentation = 0
    linesOfTokens = []

    constructor() {
        this._registerNewLine()
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
