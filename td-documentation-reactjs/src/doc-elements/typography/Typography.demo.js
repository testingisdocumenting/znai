import React from 'react'
import {elementsLibrary} from '../DefaultElementsLibrary'

export function typographyDemo(registry) {
    const headingcontent = [headingDemo()]
    const headingTextcontent = [headingTextDemo()]

    registry
        .add('headings', <elementsLibrary.DocElement content={headingcontent} elementsLibrary={elementsLibrary}/>)
        .add('headings with text', <elementsLibrary.DocElement content={headingTextcontent} elementsLibrary={elementsLibrary}/>)
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
