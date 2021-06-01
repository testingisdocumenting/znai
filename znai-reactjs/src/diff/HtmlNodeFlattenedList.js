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

import {getNodeClassName, getNodeTagName} from '../utils/domNodes'

const containerClassNames = createContainerClassesList()

export class HtmlNodeFlattenedList {
    constructor(node) {
        this.list = []
        this.idx = 0;
        this.currentContainerNode = null

        this.walkNodes(node)
    }

    walkNodes(node) {
        if (node === null) {
            return
        }

        const shouldStop = this.processNode(node)
        if (shouldStop) {
            return
        }

        const childNodesCount = node.childNodes.length
        for (let idx = 0; idx < childNodesCount; idx++) {
            if (isContainerNode(node)) {
                this.currentContainerNode = node
            }

            this.walkNodes(node.childNodes[idx])

            this.currentContainerNode = null
        }
    }

    processNode(node) {
        if (ignoreNode(node)) {
            return true
        }

        if (isTextNode(node)) {
            this.registerTextNode(node)
        } else if (isVisualNode(node)) {
            this.registerVisualNode(node)
        }

        return false
    }

    registerTextNode(node) {
        this.registerNode(node.parentNode, node.textContent)
    }

    registerVisualNode(node) {
        this.registerNode(node, attributesAsText(node))
    }

    registerNode(node, value) {
        this.list.push({
            idx: this.idx++,
            node: node,
            value: value,
            container: this.currentContainerNode
        })
    }
}

function isContainerNode(node) {
    if (!node.className) {
        return false
    }

    const nodeClass = getNodeClassName(node)
    return containerClassNames.some(className => nodeClass.indexOf(className) !== -1)
}

function isTextNode(node) {
    return node.nodeType === Node.TEXT_NODE && !!node.textContent
}

function isVisualNode(node) {
    const tagName = getNodeTagName(node)
    return tagName === 'img'
}

function ignoreNode(node) {
    return getNodeClassName(node) === 'znai-page-last-update-time'
}

function attributesAsText(node) {
    const parts = []

    for (let idx = 0; idx < node.attributes.length; idx++) {
        const attr = node.attributes[idx];
        if (attr.name === 'id' || attr.name === 'class') {
            continue
        }

        parts.push(`${attr.name}=${attr.value}`)
    }

    return parts.join(' ')
}

function createContainerClassesList() {
    return [
        'content-block',
        'wide-screen'
    ]
}
