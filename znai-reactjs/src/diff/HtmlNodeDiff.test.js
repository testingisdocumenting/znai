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

import {HtmlNodeDiff} from './HtmlNodeDiff'
import {createNode} from './nodeTestUtils'

describe('HtmlNodeDiff', () => {
    it('should extract a list of added nodes', () => {
        const beforeNode = createNode({
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

        const afterNode = createNode({
            tagName: 'div', children: [
                {
                    tagName: 'div', children: [
                        {tagName: 'div', className: 'text', text: 'line 2'}]
                },
                {
                    tagName: 'div', children: [
                        {tagName: 'div', className: 'text', text: 'line 5'},
                        {tagName: 'div', className: 'text', text: 'line 6'},
                        {tagName: 'div', className: 'text', text: 'line 3'}]
                }
            ]
        })

        const nodeDiff = new HtmlNodeDiff(beforeNode, afterNode)
        const addedNodesText = nodeDiff.addedNodes.map(node => node.innerText)

        expect(addedNodesText).toEqual(['line 5', 'line 6'])
    })
})