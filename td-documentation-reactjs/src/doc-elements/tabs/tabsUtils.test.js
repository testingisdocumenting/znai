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
                    "maxLineLength": 70,
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
                                    "maxLineLength": 136,
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
                                    "maxLineLength": 29,
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