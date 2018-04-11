import {TokensPrinter} from '../code-snippets/TokensPrinter'

export function printJsx(elementDecl) {
    const jsxPrinter = new JsxPrinter()
    jsxPrinter.printJsx(elementDecl)

    return jsxPrinter.printer.linesOfTokens
}

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