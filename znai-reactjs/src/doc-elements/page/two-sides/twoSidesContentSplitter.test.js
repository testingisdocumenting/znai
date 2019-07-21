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

import {splitContentInTwoPartsSubsections} from './twoSidesContentSplitter'

describe('splitContentInTwoPartsSubsections', () => {
    it('splits by first entry of second-side meta', () => {
        const subSections = splitContentInTwoPartsSubsections(oneSubSectionContent())

        expect(subSections.length).toEqual(1)

        const firstSubSection = subSections[0]
        expect(firstSubSection.left).toEqual([{
            "type": "Paragraph",
            "content": [
                {
                    "text": "Markdown uses ASCII to represent styles and page structure.",
                    "type": "SimpleText",
                }
            ],
        }])
        expect(firstSubSection.right).toEqual([{
            "type": "Paragraph",
            "meta": {
                "rightSide": true,
            },
            "content": [
                {
                    "text": "Another paragraph",
                    "type": "SimpleText",
                    "meta": {
                        "rightSide": true,
                    },
                }
            ],
        }])
    })
})

function oneSubSectionContent() {
    return [
        {
            "type": "Paragraph",
            "content": [
                {
                    "text": "Markdown uses ASCII to represent styles and page structure.",
                    "type": "SimpleText",
                }
            ],
        },
        {
            "type": "Paragraph",
            "meta": {
                "rightSide": true,
            },
            "content": [
                {
                    "text": "Another paragraph",
                    "type": "SimpleText",
                    "meta": {
                        "rightSide": true,
                    },
                }
            ],
        }]

}
