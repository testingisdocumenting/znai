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

import * as Diff from 'diff'

import {HtmlNodeFlattenedList} from './HtmlNodeFlattenedList'

export class HtmlNodeDiff {
    constructor(beforeNodeRoot, afterNodeRoot) {
        this.beforeNodeRoot = beforeNodeRoot
        this.afterNodeRoot = afterNodeRoot

        this.beforeNodeList = new HtmlNodeFlattenedList(this.beforeNodeRoot)
        this.afterNodeList = new HtmlNodeFlattenedList(this.afterNodeRoot)

        this.diffResultRaw = Diff.diffArrays(
            this.beforeNodeList.list,
            this.afterNodeList.list, {comparator: compareListEntry})

        this.addedNodes = this._extractAddedNodes()
    }

    attachHighlightClassToAdded(nodes) {

    }

    _extractAddedNodes() {
        return this.diffResultRaw
            .filter(diffEntry => diffEntry.added)
            .flatMap(diffEntry => diffEntry.value)
            .map(addEntryValue => addEntryValue.node)
    }
}

function compareListEntry(a, b) {
    return a.text === b.text
}