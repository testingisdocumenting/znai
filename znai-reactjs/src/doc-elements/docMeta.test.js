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

import {getDocMeta, mergeDocMeta, setDocMeta} from './docMeta.js'

describe('test methods in docMeta', () => {
    it('get docMeta after setting one', () => {
        const docMeta = {
            id: "preview",
            title: "Your Product",
            type: "Guide",
            previewEnabled: true
        }

        const expected = {
            id: "preview",
            title: "Your Product",
            type: "Guide",
            previewEnabled: true
        }
        setDocMeta(docMeta)
        expect(getDocMeta()).toEqual(expected)
    })

    it('add twice and check merged docMeta', () => {
        const supportMeta = {
            link: "https://example.com"
        }
        const docMeta = {
            id: "preview",
            title: "Your Product",
            type: "Guide",
            previewEnabled: true
        }
        mergeDocMeta({supportMeta: supportMeta})
        mergeDocMeta(docMeta)

        const expected = {
            id: "preview",
            title: "Your Product",
            type: "Guide",
            previewEnabled: true,
            supportMeta: {
                link: "https://example.com"
            }
        }

        expect(getDocMeta()).toEqual(expected)
    })

    it('set and add docMeta and check merged docMeta', () => {
        const supportMeta = {
            link: "https://example.com"
        }
        const docMeta = {
            id: "preview",
            title: "Your Product",
            type: "Guide",
            previewEnabled: true
        }
        setDocMeta(docMeta)
        mergeDocMeta({supportMeta: supportMeta})

        const expected = {
            id: "preview",
            title: "Your Product",
            type: "Guide",
            previewEnabled: true,
            supportMeta: {
                link: "https://example.com"
            }
        }

        expect(getDocMeta()).toEqual(expected)
    })

    it('add and set docMeta and check docMeta', () => {
        const supportMeta = {
            link: "https://example.com"
        }
        const docMeta = {
            id: "preview",
            title: "Your Product",
            type: "Guide",
            previewEnabled: true
        }
        mergeDocMeta({supportMeta: supportMeta})
        setDocMeta(docMeta)

        const expected = {
            id: "preview",
            title: "Your Product",
            type: "Guide",
            previewEnabled: true
        }

        expect(getDocMeta()).toEqual(expected)
    })
})