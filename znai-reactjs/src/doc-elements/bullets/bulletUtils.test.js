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

import {
    startsWithIcon,
    extractIconId,
    removeIcon,
    extractTextLinesEmphasisOnly,
    extractTextLinesEmphasisOrFull
} from './bulletUtils'

const itemContent = [
    {
        "type": "Paragraph",
        "content": [
            {
                "type": "Icon",
                "id": "time"
            },
            {
                "type": "Emphasis",
                "content": [
                    {
                        "text": "Inject",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "text": " ",
                "type": "SimpleText"
            },
            {
                "code": "DAO",
                "type": "InlinedCode"
            },
            {
                "text": " to decouple",
                "type": "SimpleText"
            }
        ]
    }
]

const lowerCasedItemContent = [
    {
        "type": "Paragraph",
        "content": [
            {
                "type": "Icon",
                "id": "time"
            },
            {
                "type": "Emphasis",
                "content": [
                    {
                        "text": "inject",
                        "type": "SimpleText"
                    }
                ]
            }
        ]
    }
]


const bulletListContent = [
    {
        "type": "ListItem",
        "content": [
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "Additional",
                        "type": "SimpleText"
                    },
                    {
                        "type": "Emphasis",
                        "content": [
                            {
                                "text": "Inject",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        "type": "ListItem",
        "content": [
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "Third Step with text",
                        "type": "SimpleText"
                    }
                ]
            }
        ]
    }
]

describe("bulletUtils", () => {
    it("detects if bullet item starts with Icon", () => {
        expect(startsWithIcon(itemContent)).toBeTruthy()
    })

    it("extracts Icon id", () => {
        expect(extractIconId(itemContent)).toEqual("time")
    })

    it("removes icon from content", () => {
        const originalSize = itemContent[0].content.length

        const withoutIcon = removeIcon(itemContent)

        expect(withoutIcon[0].content[0].type).toEqual("Emphasis")
        expect(itemContent[0].content.length).toEqual(originalSize)
    })

    it("extracts only emphasised text", () => {
        const extractedTexts = extractTextLinesEmphasisOnly(itemContent)
        expect(extractedTexts).toEqual(["Inject"])
    })

    it("extracts full text when emphasised is not present", () => {
        const extractedTexts = extractTextLinesEmphasisOrFull(bulletListContent)
        expect(extractedTexts).toEqual(["Inject", "Third Step with text"])
    })

    it("extracted text is capitalized", () => {
        const extractedTexts = extractTextLinesEmphasisOnly(lowerCasedItemContent)
        expect(extractedTexts).toEqual(["Inject"])
    })
})