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

const Diff = require('diff')


class FlattenedView {
    constructor() {
        this.list = []
        this.currentIdx = 0
    }

    add(node, associatedText) {
        this.list.push({idx: this.currentIdx, node: node, text: associatedText})
        this.currentIdx++
    }
}

function compareListEntry(a, b) {
    return a.text === b.text
}

describe('Page Diff', () => {
    it('should flatten entries, one per line', () => {

        const left = new FlattenedView()
        left.add('node1', 'hello')
        left.add('node2', 'world')
        left.add('node3', 'of lines')

        const right = new FlattenedView()
        right.add('node4', 'extra in front')
        right.add('node4', 'hello')
        right.add('node5', 'world2')
        right.add('node6', 'some in the middle')
        right.add('node6', 'of lines')

        const result = Diff.diffArrays(left.list, right.list, {comparator: compareListEntry})
        console.log(JSON.stringify(result, null, 2))
    })
})