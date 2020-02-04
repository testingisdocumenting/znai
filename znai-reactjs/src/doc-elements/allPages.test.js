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

import {AllPages} from './allPages'

describe('all pages', () => {
    it('update page', () => {
        const allPages = new AllPages([
            {
                tocItem: {
                    dirName: 'd1',
                    fileName: 'f1',
                },
                content: 'c1'
            },
            {
                tocItem: {
                    dirName: 'd2',
                    fileName: 'f2',
                },
                content: 'c2'
            }
        ])

        allPages.update(
            {
                tocItem: {
                    dirName: 'd1',
                    fileName: 'f1',
                },
                content: 'c1-'
            }
        )

        expect(allPages.pages).toEqual([
            {tocItem: {dirName: 'd1', fileName: 'f1'}, content: 'c1-'},
            {tocItem: {dirName: 'd2', fileName: 'f2'}, content: 'c2'}
        ])
    })

    it('add page', () => {
        const allPages = new AllPages([
            {
                tocItem: {
                    dirName: 'd1',
                    fileName: 'f1',
                },
                content: 'c1'
            },
            {
                tocItem: {
                    dirName: 'd2',
                    fileName: 'f2',
                },
                content: 'c2'
            }
        ])

        allPages.update(
            {
                tocItem: {
                    dirName: 'd3',
                    fileName: 'f3',
                },
                content: 'c3'
            }
        )

        expect(allPages.pages).toEqual([
            {tocItem: {dirName: 'd1', fileName: 'f1'}, content: 'c1'},
            {tocItem: {dirName: 'd2', fileName: 'f2'}, content: 'c2'},
            {tocItem: {dirName: 'd3', fileName: 'f3'}, content: 'c3'}
        ])
    })

    it('remove page', () => {
        const allPages = new AllPages([
            {
                tocItem: {
                    dirName: 'd1',
                    fileName: 'f1',
                },
                content: 'c1'
            },
            {
                tocItem: {
                    dirName: 'd2',
                    fileName: 'f2',
                },
                content: 'c2'
            },
            {
                tocItem: {
                    dirName: 'd3',
                    fileName: 'f3',
                },
                content: 'c3'
            }
        ])

        allPages.remove({dirName: 'd2', fileName: 'f2'})

        expect(allPages.pages).toEqual(
            [
                {tocItem: {dirName: 'd1', fileName: 'f1'}, content: 'c1'},
                {tocItem: {dirName: 'd3', fileName: 'f3'}, content: 'c3'}
            ]
        )
    })
})