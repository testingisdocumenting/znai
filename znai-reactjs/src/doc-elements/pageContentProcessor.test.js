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
                                "type": "SimpleText",
                                "meta": {
                                    "extraPropB": "specific"
                                }
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
                                    "extraPropB": "specific"
                                }
                            }
                        ],
                    }]
            }]

        const actual = pageContentProcessor.process(original)
        expect(actual).toEqual(expected)
    })

    it('should extract footnotes from content', () => {
        const content = [
            {
                "title": "Title",
                "id": "title",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {"text": "Some text", "type": "SimpleText"},
                            {"type": "FootnoteReference", "label": "1", "content": [{"type": "Paragraph", "content": [{"text": "first note", "type": "SimpleText"}]}]},
                            {"text": " more text", "type": "SimpleText"},
                            {"type": "FootnoteReference", "label": "2", "content": [{"type": "Paragraph", "content": [{"text": "second note", "type": "SimpleText"}]}]}
                        ]
                    }
                ]
            }]

        const footnotes = pageContentProcessor.extractFootnotes(content)

        expect(footnotes).toEqual([
            {label: "1", refCount: 1, content: [{"type": "Paragraph", "content": [{"text": "first note", "type": "SimpleText"}]}]},
            {label: "2", refCount: 1, content: [{"type": "Paragraph", "content": [{"text": "second note", "type": "SimpleText"}]}]}
        ])
    })

    it('should extract footnotes across multiple sections', () => {
        const content = [
            {
                "title": "Section One",
                "id": "section-one",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {"type": "FootnoteReference", "label": "1", "content": [{"type": "Paragraph", "content": [{"text": "note one", "type": "SimpleText"}]}]}
                        ]
                    }
                ]
            },
            {
                "title": "Section Two",
                "id": "section-two",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {"type": "FootnoteReference", "label": "2", "content": [{"type": "Paragraph", "content": [{"text": "note two", "type": "SimpleText"}]}]}
                        ]
                    }
                ]
            }]

        const footnotes = pageContentProcessor.extractFootnotes(content)
        expect(footnotes).toHaveLength(2)
        expect(footnotes[0].label).toEqual("1")
        expect(footnotes[1].label).toEqual("2")
    })

    it('should return empty array when no footnotes present', () => {
        const content = [
            {
                "title": "Title",
                "id": "title",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {"text": "Simple text", "type": "SimpleText"}
                        ]
                    }
                ]
            }]

        const footnotes = pageContentProcessor.extractFootnotes(content)
        expect(footnotes).toHaveLength(0)
    })

    it('should deduplicate footnotes with same label and track refCount', () => {
        const noteContent = [{"type": "Paragraph", "content": [{"text": "shared note", "type": "SimpleText"}]}]
        const content = [
            {
                "title": "Title",
                "id": "title",
                "type": "Section",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {"type": "FootnoteReference", "label": "1", "content": noteContent},
                            {"type": "FootnoteReference", "label": "1", "content": noteContent},
                            {"type": "FootnoteReference", "label": "1", "content": noteContent}
                        ]
                    }
                ]
            }]

        const footnotes = pageContentProcessor.extractFootnotes(content)
        expect(footnotes).toHaveLength(1)
        expect(footnotes[0].label).toEqual("1")
        expect(footnotes[0].refCount).toEqual(3)
    })
})