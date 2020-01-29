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
    it('should extract a list of added nodes and their containers', () => {
        const beforeNode = createNode({
            tagName: 'div', children: [
                {
                    id: 'c1', tagName: 'div', className: 'content-block', children: [
                        {tagName: 'div', className: 'text', text: 'line 1'},
                        {tagName: 'div', className: 'text', text: 'line 2'}]
                },
                {
                    id: 'c2', tagName: 'div', className: 'wide-screen', children: [
                        {tagName: 'div', className: 'text', text: 'line 3'},
                        {tagName: 'div', className: 'text', text: 'line 4'}]
                }
            ]
        })

        const afterNode = createNode({
            tagName: 'div', children: [
                {
                    tagName: 'div', children: [
                        {tagName: 'div', className: 'text', text: 'line 2'},
                        {id: 't1', tagName: 'div', className: 'text', text: 'line 21'}]
                },
                {
                    id: 'c2', tagName: 'div', className: 'wide-screen', children: [
                        {id: 't2', tagName: 'div', className: 'text', text: 'line 3'},
                        {id: 't3', tagName: 'div', className: 'text', text: 'line 5'},
                        {id: 't4', tagName: 'div', className: 'text', text: 'line 6'},
                        {id: 't5', tagName: 'div', className: 'text', text: 'line 4'}]
                }
            ]
        })

        const nodeDiff = new HtmlNodeDiff(beforeNode, afterNode)

        const addedNodeIds = nodeDiff.addedNodes.map(node => node.id)
        expect(addedNodeIds).toEqual(['t1', 't3', 't4'])

        const containersIds = nodeDiff.addedNodesContainers.map(node => node.id)
        expect(containersIds).toEqual(['c2'])
    })
})