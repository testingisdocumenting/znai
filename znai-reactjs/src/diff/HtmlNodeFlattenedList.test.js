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

import {HtmlNodeFlattenedList} from './HtmlNodeFlattenedList'
import {createNode} from './nodeTestUtils'

describe('HtmlNodeFlattenedList', () => {
    it('should build a flat list of nodes and associated text', () => {
        const root = createNode({
            tagName: 'div', children: [
                {
                    tagName: 'div', children: [
                        {tagName: 'div', className: 'text', text: 'line 1'},
                        {tagName: 'div', className: 'text', text: 'line 2'}]
                },
                {
                    tagName: 'div', children: [
                        {tagName: 'div', className: 'text', text: 'line 3'},
                        {tagName: 'div', className: 'text', text: 'line 4'}]
                }
            ]
        })

        const nodeList = new HtmlNodeFlattenedList(root)

        const simplifiedActual = nodeList.list.map(e => ({idx: e.idx, innerText: e.node.innerText, text: e.text}))
        expect(simplifiedActual).toEqual([
            {
                "idx": 0,
                "innerText": "line 1",
                "text": "line 1"
            },
            {
                "idx": 1,
                "innerText": "line 2",
                "text": "line 2"
            },
            {
                "idx": 2,
                "innerText": "line 3",
                "text": "line 3"
            },
            {
                "idx": 3,
                "innerText": "line 4",
                "text": "line 4"
            }
        ])

    })
})
