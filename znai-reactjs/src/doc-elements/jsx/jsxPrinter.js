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

import {TokensPrinter} from '../code-snippets/TokensPrinter'

class JsxPrinter {
    printer = new TokensPrinter()

    printJsx(elementDecl) {
        this.printer.printDelimiter('<')
        this.printTag(elementDecl.tagName)

        if (elementDecl.attributes.length > 1) {
            this.printMultiLineAttrs(elementDecl.attributes)
        } else if (elementDecl.attributes.length === 1) {
            this.printSingleAttr(elementDecl.attributes[0])
        }

        this.printer.printDelimiter('/>')
    }

    printSingleAttr(attr) {
        this.printer.printDelimiter(' ')
        this.printNameValue(attr)
    }

    printMultiLineAttrs(attrs) {
        this.printer.println()
        this.printer.indentRight()

        attrs.forEach(attr => {
            this.printer.printIndentation()
            this.printNameValue(attr)

            this.printer.println()
        })

        this.printer.indentLeft()
    }

    printNameValue(attr) {
        this.printAttrName(attr.name)
        this.printer.printDelimiter('=')
        this.printAttrValue(attr.value)
    }

    printTag(tagName) {
        this.printer.print('tag', tagName)
    }

    printAttrName(name) {
        this.printer.print('attr-name', name)
    }

    printAttrValue(value) {
        this.printer.print('attr-value', value)
    }
}

export function printJsx(elementDecl) {
    const jsxPrinter = new JsxPrinter()
    jsxPrinter.printJsx(elementDecl)

    return jsxPrinter.printer.linesOfTokens
}
