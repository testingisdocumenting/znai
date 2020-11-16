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

    flushLine() {
        this._registerNewLine()
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
