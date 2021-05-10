/*
 * Copyright 2021 znai maintainers
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

class XmlPrinter {
    printer = new TokensPrinter()

    constructor({singleLineAttrs, pathsToHighlight = []}) {
        this.singleLineAttrs = singleLineAttrs

        this._pathsToHighlight = {}
        pathsToHighlight.forEach(p => this._pathsToHighlight[p] = true)
    }

    printXml(path, xmlAsJson) {
        this.printer.printIndentation()
        this.printer.printDelimiter('<')
        this.printTag(xmlAsJson.tagName)

        this.printAttrs(path, xmlAsJson.attributes)

        const numberOfChildren = (xmlAsJson.children || []).length
        const hasChildren = numberOfChildren > 0

        const numberOfChildrenWithTags = numberOfNodesWithTags(xmlAsJson.children)

        if (hasChildren) {
            this.printer.printDelimiter('>')
            this.printChildren(path, xmlAsJson.children)

            if (numberOfChildrenWithTags > 0) {
                this.printer.printIndentation()
            }

            this.printer.printDelimiter('</')
            this.printTag(xmlAsJson.tagName)
            this.printer.printDelimiter('>')
        } else {
            this.printer.printDelimiter('/>')
        }
        // this.printer.println()
    }

    printAttrs(path, attributes) {
        if (!attributes || !attributes.length) {
            return
        }

        const isMultiLineAttrs = !this.singleLineAttrs && attributes.length > 1
        if (isMultiLineAttrs) {
            this.printMultiLineAttrs(path, attributes)
            this.printer.printIndentation()
        } else {
            this.printSingleAttrs(path, attributes)
        }
    }

    printChildren(path, children) {
        if (!children || !children.length) {
            return
        }

        const isSingleChild = children.length === 1
        const isFirstChildText = children[0].tagName === ''

        if (isSingleChild && isFirstChildText) {
            this.printText(path, children[0].text)
            return
        }

        this.printer.indentRight()
        this.printer.println()

        const numberOfChildrenWithTags = numberOfNodesWithTags(children)

        const tagIdx = {}
        children.forEach(c => {
            tagIdx[c.tagName] = 0
        })

        children.forEach(c => {
            const tagNameForPath = c.tagName ? c.tagName : '#text'
            const childrenPath = path + '.' + tagNameForPath + (numberOfChildrenWithTags > 1 ?
                '[' + tagIdx[c.tagName]++ + ']' :
                '');

            if (c.tagName) {
                this.printXml(childrenPath, c)
                this.printer.println()
            } else {
                this.printer.printIndentation()
                this.printText(childrenPath, c.text)
                this.printer.println()
            }
        })
        this.printer.indentLeft()
    }

    printText(path, text) {
        const tokenType = 'text' + (this.isHighlightedPath(path) ? ' highlighted' : '')
        this.printer.print(tokenType, text)
    }

    printSingleAttrs(path, attributes) {
        attributes.forEach(attr => {
            this.printer.printDelimiter(' ')
            this.printNameValue(path, attr)
        })
    }

    printMultiLineAttrs(path, attrs) {
        this.printer.println()
        this.printer.indentRight()

        attrs.forEach(attr => {
            this.printer.printIndentation()
            this.printNameValue(path, attr)

            this.printer.println()
        })

        this.printer.indentLeft()
    }

    printNameValue(path, attr) {
        this.printAttrName(attr.name)
        this.printer.printDelimiter('=')
        this.printAttrValue(path + '.@' + attr.name, attr.value)
    }

    printTag(tagName) {
        this.printer.print('tag', tagName)
    }

    printAttrName(name) {
        this.printer.print('attr-name', name)
    }

    printAttrValue(path, value) {
        const tokenType = 'attr-value' + (this.isHighlightedPath(path) ? ' highlighted' : '')
        this.printer.print(tokenType, value)
    }

    isHighlightedPath(path) {
        return this._pathsToHighlight.hasOwnProperty(path)
    }
}

function numberOfNodesWithTags(nodes) {
    if (!nodes) {
        return 0
    }

    let result = 0
    nodes.forEach(n => {
        if (n.tagName) {
            result++
        }
    })

    return result
}

export function printXml({xmlAsJson, pathsToHighlight, singleLineAttrs}) {
    const xmlPrinter = new XmlPrinter({singleLineAttrs, pathsToHighlight})
    xmlPrinter.printXml(xmlAsJson.tagName, xmlAsJson)

    return xmlPrinter.printer.linesOfTokens
}
