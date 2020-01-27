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
                    id: 'c1', tagName: 'div', className: 'content-block', children: [
                        {id: 't1', tagName: 'div', className: 'text', text: 'line 1'},
                        {id: 'img1', tagName: 'img', className: 'annotated', attrs: {src: 'path', timestamp: 'time'}},
                        {id: 't2', tagName: 'div', className: 'text', text: 'line 2'}]
                },
                {
                    id: 'no-container', tagName: 'div', children: [
                        {id: 'noc1', tagName: 'div', className: 'text', text: 'line 01'},
                        {id: 'noc2', tagName: 'div', className: 'text', text: 'line 02'}]
                },
                {
                    id: 'c2', tagName: 'div', className: 'wide-screen', children: [
                        {id: 't3', tagName: 'div', className: 'text', text: 'line 3'},
                        {id: 't4', tagName: 'div', className: 'text', text: 'line 4'}]
                }
            ]
        })

        const nodeList = new HtmlNodeFlattenedList(root)

        const simplifiedActual = nodeList.list.map(e => (
            {
                idx: e.idx,
                value: e.value,
                nodeId: e.node.id,
                containerId: e.container && e.container.id
            }
        ))

        expect(simplifiedActual).toEqual([
            {
                idx: 0,
                value: 'line 1',
                nodeId: 't1',
                containerId: 'c1'
            },
            {
                idx: 1,
                value: 'src=path timestamp=time',
                nodeId: 'img1',
                containerId: 'c1'
            },
            {
                idx: 2,
                value: 'line 2',
                nodeId: 't2',
                containerId: 'c1'
            },
            {
                idx: 3,
                value: 'line 01',
                nodeId: 'noc1',
                containerId: null
            },
            {
                idx: 4,
                value: 'line 02',
                nodeId: 'noc2',
                containerId: null
            },
            {
                idx: 5,
                value: 'line 3',
                nodeId: 't3',
                containerId: 'c2'
            },
            {
                idx: 6,
                value: 'line 4',
                nodeId: 't4',
                containerId: 'c2'
            }
        ])

    })
})
