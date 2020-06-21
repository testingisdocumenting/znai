/*
 * Copyright 2020 znai maintainers
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

import {pageContentProcessor} from './pageContentProcessor.js'

describe('page content post processor', () => {
    it('should create an empty section if no section is defined', () => {
        const original = [
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "Markdown uses ASCII to represent styles and page structure.",
                        "type": "SimpleText"
                    }
                ]
            }]

        const expected = [{
            "title": "",
            "id": "",
            "type": "Section",
            "content": [
                {
                    "type": "Paragraph",
                    "content": [
                        {
                            "text": "Markdown uses ASCII to represent styles and page structure.",
                            "type": "SimpleText"
                        }
                    ]
                }]}]

        const actual = pageContentProcessor.process(original)
        expect(actual).toEqual(expected)
    })

    it('should merge meta props into a next doc element and all its children', () => {
        const original = [
            {
                "title": "Title",
                "id": "title",
                "type": "Section",
                "content": [
                    {
                        "type": "Meta",
                        "extraPropA": "value1",
                        "extraPropB": "value2"
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Markdown uses ASCII to represent styles and page structure.",
                                "type": "SimpleText"
                            }
                        ]
                    },
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "Another paragraph",
                                "type": "SimpleText"
                            }
                        ]
                    }]
            }]

        const expected  = [
            {
                "title": "Title",
                "id": "title",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "meta": {
                            "extraPropA": "value1",
                            "extraPropB": "value2"
                        },
                        "content": [
                            {
                                "text": "Markdown uses ASCII to represent styles and page structure.",
                                "type": "SimpleText",
                                "meta": {
                                    "extraPropA": "value1",
                                    "extraPropB": "value2"
                                }
                            }
                        ],
                    },
                    {
                        "type": "Paragraph",
                        "meta": {
                            "extraPropA": "value1",
                            "extraPropB": "value2"
                        },
                        "content": [
                            {
                                "text": "Another paragraph",
                                "type": "SimpleText",
                                "meta": {
                                    "extraPropA": "value1",
                                    "extraPropB": "value2"
                                }
                            }
                        ],
                    }]
            }]

        const actual = pageContentProcessor.process(original)
        expect(actual).toEqual(expected)
    })
})