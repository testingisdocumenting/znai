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

import {paragraphStartsWith, removeSuffixFromParagraph} from './paragraphUtils'

const paragraphContent = [
    {
        "text": "Note: It is very easy to add a code snippet or an output result.",
        "type": "SimpleText"
    }
]

const paragraphWithSpacesAtStartContent = [
    {
        "text": "  Note: It is very easy to add a code snippet or an output result.",
        "type": "SimpleText"
    }
]

describe("paragraphUtils", () => {
    it("detects if paragraph starts with a specified text", () => {
        expect(paragraphStartsWith(paragraphContent, "Note:")).toBeTruthy()
        expect(paragraphStartsWith(paragraphWithSpacesAtStartContent, "Note:")).toBeTruthy()
        expect(paragraphStartsWith([], "Note:")).toBeFalsy()
        expect(paragraphStartsWith(paragraphContent, "Warning:")).toBeFalsy()
    })

    it("removes a suffix from text element within a paragraph", () => {
        const expected = [{
            "text": "It is very easy to add a code snippet or an output result.",
            "type": "SimpleText" }]

        expect(removeSuffixFromParagraph(paragraphContent, "Note:")).toEqual(expected)
        expect(removeSuffixFromParagraph(paragraphWithSpacesAtStartContent, "Note:")).toEqual(expected)

        expect(removeSuffixFromParagraph([{
                "text": "Note:It is very easy to add a code snippet or an output result.",
                "type": "SimpleText"
                }], "Note:")).toEqual(expected)
    })

    it("removes a text element within a paragraph if the only text is a suffix", () => {
        const text = {
            "text": "Note:",
            "type": "SimpleText" }

        const textWithExtraSpace = {
            "text": "Note: ",
            "type": "SimpleText" }

        const inlinedCode = {
            "code": "ExchangeCalendar",
            "type": "InlinedCode" }

        expect(removeSuffixFromParagraph([text, inlinedCode], "Note:")).toEqual([inlinedCode])
        expect(removeSuffixFromParagraph([textWithExtraSpace, inlinedCode], "Note:")).toEqual([inlinedCode])
    })
})