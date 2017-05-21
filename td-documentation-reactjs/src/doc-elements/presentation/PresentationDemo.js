import React from 'react'

import {elementsLibrary, presentationElementHandlers} from '../DefaultElementsLibrary'

import Presentation from './Presentation'
import PresentationRegistry from './PresentationRegistry'

const quoteContent = [
    {
        "type": "Paragraph",
        "content": [
            {
                "content": [{"text": "test etest", type: "SimpleText"}],
                "type": "BlockQuote"
            },
        ]
    },
    {
        "type": "Paragraph",
        "content": [
            {
                "content": [{"text": "long long block quote. long long block quote. long long block quote. long long block quote. long long block quote. long long block quote", type: "SimpleText"}],
                "type": "BlockQuote"
            },
        ]
    }
]

const bulletContent = [
    {
        "bulletMarker": "*",
        "tight": true,
        "type": "BulletList",
        "content": [
            {
                "type": "ListItem",
                "content": [
                    {
                        "type": "Paragraph",
                        "content": [
                            {
                                "text": "users\u0027 time lost",
                                "type": "SimpleText"
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
                                "text": "reputation damage",
                                "type": "SimpleText"
                            }
                        ]
                    }
                ]
            }
        ]
    }
]

const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, bulletContent)
const PresentationDemo = (props) => <Presentation presentationRegistry={registry}/>

export default PresentationDemo

