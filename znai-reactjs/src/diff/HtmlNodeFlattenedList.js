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

const classesToSkip = createClassesToSkipDict()

export class HtmlNodeFlattenedList {
    constructor(node) {
        this.root = node
        this.list = []
        this.idx = 0;

        this._walkNodes()
    }

    _walkNodes() {
        let treeWalker = document.createTreeWalker(this.root)

        for (;;) {
            let node = treeWalker.nextNode()
            if (!node) {
                break
            }

            if (ignoreNode(node)) {
                continue
            }

            this.registerNode(node)
        }
    }

    registerNode(node) {
        this.list.push({
            idx: this.idx++,
            node: node,
            text: node.innerText
        })
    }
}

function ignoreNode(node) {
    const classes = (typeof node.className === 'string') ? node.className.split(' ').filter((cn) => cn.length) : []
    if (! classes.length) {
        return true
    }

    return classes.some(className => classesToSkip[className])
}

function createClassesToSkipDict() {
    const dict = {}

    const classNames = [
        'section',
        'content-block',
        'page-title-block',
        'page-meta-block',
        'page-last-update-time',
        'two-sides-page-content',
        'page-two-sides-layout',
        'page-two-sides-left-part',
        'page-two-sides-right-part',
        'two-sides-section'
    ]

    classNames.forEach(className =>
        dict[className] = true
    )

    return dict
}

