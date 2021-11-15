/*
 * Copyright 2021 znai maintainers
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

import React from "react";
import { elementsLibrary } from "../DefaultElementsLibrary";

export function typographyDemo(registry) {
    registry
        .add('headings', () => <elementsLibrary.DocElement content={[headingDemo()]} elementsLibrary={elementsLibrary}/>)
        .add('headings with badges', () => <elementsLibrary.DocElement content={[headingDemoWithBadge()]} elementsLibrary={elementsLibrary}/>)
        .add('headings types', () => <elementsLibrary.DocElement content={[headingDemoTypes()]} elementsLibrary={elementsLibrary}/>)
        .add('headings with text', () => <elementsLibrary.DocElement content={[headingTextDemo()]}
                                                                     elementsLibrary={elementsLibrary}/>)
}

function headingDemo() {
    return {
        "title": "Section Heading",
        "id": "section-heading",
        "type": "Section",
        "content": [
            {
                "level": 2,
                "type": "SubHeading",
                "title": "Sub-Section Heading",
                "id": "sub-section-heading",
            },
            {
                "level": 3,
                "type": "SubHeading",
                "title": "Sub-Sub-Section Heading",
                "id": "sub-sub-section-heading",
            },
            {
                "level": 4,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-section-heading",
            },
            {
                "level": 5,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-sub-section-heading",
            },
            {
                "level": 6,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-sub-sub-section-heading"
            }
        ]
    }
}

function headingDemoWithBadge() {
    return {
        "title": "Section Heading",
        "id": "section-heading",
        "type": "Section",
        "badge": "v1.20",
        "content": [
            {
                "level": 2,
                "type": "SubHeading",
                "title": "Sub-Section Heading",
                "id": "sub-section-heading",
                "badge": "v2.38"
            },
            {
                "level": 3,
                "type": "SubHeading",
                "title": "Sub-Sub-Section Heading",
                "id": "sub-sub-section-heading",
            },
            {
                "level": 4,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-section-heading",
                "badge": "v2.38"
            },
            {
                "level": 5,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-sub-section-heading",
            },
            {
                "level": 6,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-sub-sub-section-heading"
            }
        ]
    }
}

function headingDemoTypes() {
    return {
        "title": "Section Heading",
        "id": "section-heading",
        "type": "Section",
        "style": "api",
        "content": [
            {
                "level": 2,
                "type": "SubHeading",
                "title": "Sub-Section Heading",
                "id": "sub-section-heading",
                "style": "api"
            },
            {
                "level": 3,
                "type": "SubHeading",
                "title": "Sub-Sub-Section Heading",
                "id": "sub-sub-section-heading",
                "style": "api"
            },
            {
                "level": 4,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-section-heading",
                "style": "api"
            },
            {
                "level": 5,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-sub-section-heading",
                "style": "api"
            },
            {
                "level": 6,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-sub-sub-section-heading",
                "style": "api"
            }
        ]
    }
}

function headingTextDemo() {
    return {
        "title": "Section Heading",
        "id": "section-heading",
        "type": "Section",
        "content": [
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though. Rather carried down alive to wondrous depths, where strange shapes of the unwarped primal world glided to and fro before his passive eyes; and the miser-merman, Wisdom, revealed his hoarded heaps; and among the joyous, heartless, ever-juvenile eternities, Pip saw the multitudinous, God-omnipresent, coral insects, that out of the firmament of waters heaved the colossal orbs.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "He saw God’s foot upon the treadle of the loom, and spoke it; and therefore his shipmates called him mad. So man’s insanity is heaven’s sense; and wandering from all mortal reason, man comes at last to that celestial thought, which, to reason, is absurd and frantic; and weal or woe, feels then uncompromised, indifferent as his God.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "ThematicBreak",
                "content": []
            },
            {
                "level": 2,
                "type": "SubHeading",
                "title": "Sub-Section Heading",
                "id": "sub-section-heading"
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "Rather carried down alive to wondrous depths, where strange shapes of the unwarped primal world glided to and fro before his passive eyes; and the miser-merman, Wisdom, revealed his hoarded heaps; and among the joyous, heartless, ever-juvenile eternities, Pip saw the multitudinous, God-omnipresent, coral insects, that out of the firmament of waters heaved the colossal orbs.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "He saw God’s foot upon the treadle of the loom, and spoke it; and therefore his shipmates called him mad.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "So man’s insanity is heaven’s sense; and wandering from all mortal reason, man comes at last to that celestial thought, which, to reason, is absurd and frantic; and weal or woe, feels then uncompromised, indifferent as his God.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "ThematicBreak",
                "content": []
            },
            {
                "level": 3,
                "type": "SubHeading",
                "title": "Sub-Sub-Section Heading",
                "id": "sub-sub-section-heading",
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though. Rather carried down alive to wondrous depths, where strange shapes of the unwarped primal world glided to and fro before his passive eyes; and the miser-merman, Wisdom, revealed his hoarded heaps; and among the joyous, heartless, ever-juvenile eternities, Pip saw the multitudinous, God-omnipresent, coral insects, that out of the firmament of waters heaved the colossal orbs. He saw God’s foot upon the treadle of the loom, and spoke it; and therefore his shipmates called him mad. So man’s insanity is heaven’s sense; and wandering from all mortal reason, man comes at last to that celestial thought, which, to reason, is absurd and frantic; and weal or woe, feels then uncompromised, indifferent as his God.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "type": "ThematicBreak",
                "content": []
            },
            {
                "level": 4,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-section-heading"
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though. Rather carried down alive to wondrous depths, where strange shapes of the unwarped primal world glided to and fro before his passive eyes; and the miser-merman, Wisdom, revealed his hoarded heaps; and among the joyous, heartless, ever-juvenile eternities, Pip saw the multitudinous, God-omnipresent, coral insects, that out of the firmament of waters heaved the colossal orbs. He saw God’s foot upon the treadle of the loom, and spoke it; and therefore his shipmates called him mad. So man’s insanity is heaven’s sense; and wandering from all mortal reason, man comes at last to that celestial thought, which, to reason, is absurd and frantic; and weal or woe, feels then uncompromised, indifferent as his God.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "level": 5,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-sub-section-heading"
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though. Rather carried down alive to wondrous depths, where strange shapes of the unwarped primal world glided to and fro before his passive eyes; and the miser-merman, Wisdom, revealed his hoarded heaps; and among the joyous, heartless, ever-juvenile eternities, Pip saw the multitudinous, God-omnipresent, coral insects, that out of the firmament of waters heaved the colossal orbs. He saw God’s foot upon the treadle of the loom, and spoke it; and therefore his shipmates called him mad. So man’s insanity is heaven’s sense; and wandering from all mortal reason, man comes at last to that celestial thought, which, to reason, is absurd and frantic; and weal or woe, feels then uncompromised, indifferent as his God.",
                        "type": "SimpleText"
                    }
                ]
            },
            {
                "level": 6,
                "type": "SubHeading",
                "title": "Sub-Sub-Sub-Sub-Sub-Section Heading",
                "id": "sub-sub-sub-sub-sub-section-heading"
            },
            {
                "type": "Paragraph",
                "content": [
                    {
                        "text": "The sea had jeeringly kept his finite body up, but drowned the infinite of his soul. Not drowned entirely, though. Rather carried down alive to wondrous depths, where strange shapes of the unwarped primal world glided to and fro before his passive eyes; and the miser-merman, Wisdom, revealed his hoarded heaps; and among the joyous, heartless, ever-juvenile eternities, Pip saw the multitudinous, God-omnipresent, coral insects, that out of the firmament of waters heaved the colossal orbs. He saw God’s foot upon the treadle of the loom, and spoke it; and therefore his shipmates called him mad. So man’s insanity is heaven’s sense; and wandering from all mortal reason, man comes at last to that celestial thought, which, to reason, is absurd and frantic; and weal or woe, feels then uncompromised, indifferent as his God.",
                        "type": "SimpleText"
                    }
                ]
            }
        ]
    }
}
