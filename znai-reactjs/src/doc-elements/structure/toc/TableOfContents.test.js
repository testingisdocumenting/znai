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

import {tableOfContents} from './TableOfContents'

describe('Table of Contents', () => {
    beforeEach(() => {
        tableOfContents.toc =
            [
                {
                    "sectionTitle": "",
                    "dirName": "",
                    "items": [{
                        "sectionTitle": "",
                        "pageTitle": "Index",
                        "pageMeta": {},
                        "fileName": "index",
                        "dirName": "",
                    }]
                },
                {
                    "sectionTitle": "REST",
                    "dirName": "REST",
                    "items": [{
                        "sectionTitle": "REST",
                        "pageTitle": "Getting Started",
                        "pageMeta": {},
                        "fileName": "getting-started",
                        "dirName": "REST",
                    }, {
                        "sectionTitle": "REST",
                        "pageTitle": "CRUD",
                        "pageMeta": {},
                        "fileName": "CRUD",
                        "dirName": "REST"
                    }]
                },
            ]
    })

    it('should detect if page is index', () => {
        expect(tableOfContents.isIndex({dirName: '', fileName: 'index'})).toBeTruthy()
        expect(tableOfContents.isIndex({dirName: '', fileName: ''})).toBeFalsy()
        expect(tableOfContents.isIndex({dirName: 'abc', fileName: 'index'})).toBeFalsy()
    })

    it('should find next toc item', () => {
        expect(tableOfContents.nextTocItem({dirName: 'REST', fileName: 'getting-started'}).pageTitle).toEqual('CRUD')
        expect(tableOfContents.nextTocItem({dirName: 'REST', fileName: 'CRUD'})).toEqual(null)
    })

    it('should find prev toc item', () => {
        expect(tableOfContents.prevTocItem({dirName: 'REST', fileName: 'CRUD'}).pageTitle).toEqual('Getting Started')
        expect(tableOfContents.prevTocItem({dirName: 'REST', fileName: 'getting-started'})).toEqual(null)
    })
})