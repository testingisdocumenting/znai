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

import {contentTabNames} from './tabsUtils'

describe('tabsUtils', () => {
    it('detects tab names from a content', () => {
        expect(contentTabNames(pageContent())).toEqual(["JavaScript", "Java", "Cpp"])
    })
})

function pageContent() {
    return [
        {
            content: []
        },
        {
            "title": "Definition",
            "id": "definition",
            "type": "Section",
            "content": [
                {
                    "type": "Paragraph",
                    "content": [
                        {
                            "text": "To define multiple tabs we use fenced code block",
                            "type": "SimpleText"
                        }
                    ]
                },
                {
                    "lang": "",
                    "tokens": [
                        {
                            "type": "text",
                            "content": ""
                        }
                    ],
                    "lineNumber": "",
                    "type": "Snippet"
                },
                {
                    "type": "Paragraph",
                    "content": [
                        {
                            "text": "This will generate a multi tab widget with an include plugin content per tab",
                            "type": "SimpleText"
                        }
                    ]
                },
                {
                    "rightSide": true,
                    "type": "Meta"
                },
                {
                    "tabsContent": [
                        {
                            "name": "JavaScript",
                            "content": []                        },
                        {
                            "name": "Java",
                            "content": [
                                {
                                    "lang": "java",
                                    "tokens": [],
                                    "wide": true,
                                    "type": "Snippet"
                                }
                            ]
                        },
                        {
                            "name": "Cpp",
                            "content": [
                                {
                                    "lang": "cpp",
                                    "tokens": [],
                                    "type": "Snippet"
                                }
                            ]
                        }
                    ],
                    "type": "Tabs",
                    "meta": {"rightSide": true}
                },
                {
                    "type": "Paragraph",
                    "content": [
                        {
                            "text": "Selecting a tab will switch all the tabs on every page.",
                            "type": "SimpleText"
                        }
                    ]
                }
            ]
        }
    ]
}