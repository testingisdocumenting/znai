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

const codeWithInlinedComments = [{
    "lang": "javascript",
    "maxLineLength": 52,
    "tokens": [
        {
            "type": "keyword",
            "content": "class"
        },
        " ",
        {
            "type": "class-name",
            "content": [
                "JsClass"
            ]
        },
        " ",
        {
            "type": "punctuation",
            "content": "{"
        },
        "\n    ",
        {
            "type": "function",
            "content": "constructor"
        },
        {
            "type": "punctuation",
            "content": "("
        },
        {
            "type": "punctuation",
            "content": ")"
        },
        " ",
        {
            "type": "punctuation",
            "content": "{"
        },
        " ",
        {
            "type": "comment",
            "content": "// new syntax for constructor"
        },
        "\n    ",
        {
            "type": "punctuation",
            "content": "}"
        },
        "\n",
        {
            "type": "punctuation",
            "content": "}"
        },
        "\n\n",
        {
            "type": "keyword",
            "content": "export"
        },
        " ",
        {
            "type": "keyword",
            "content": "default"
        },
        " JsClass ",
        {
            "type": "comment",
            "content": "// new syntax for ES6 modules"
        }
    ],
    "commentsType": "inline",
    "type": "Snippet"
}]

const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, codeWithInlinedComments)
const PresentationDemo = (props) => <Presentation presentationRegistry={registry}/>

export default PresentationDemo
