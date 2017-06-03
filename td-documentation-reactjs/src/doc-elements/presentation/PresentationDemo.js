import React from 'react'

import {elementsLibrary, presentationElementHandlers} from '../DefaultElementsLibrary'

import Presentation from './Presentation'
import PresentationRegistry from './PresentationRegistry'

import '../DocumentationLayout.css'

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

const section1 = {
    title: "Section One",
    type: "Section",
    content: codeWithInlinedComments
}

const section2 = {
    title: "Section Two",
    type: "Section",
    content: codeWithInlinedComments
}

const page = {
    tocItem: {pageTitle: "Page Title"},
    type: "Page",
    content: [section1, section2]
}

const docMeta = {id: "mdoc", title: "MDoc", type: "User Guide"}

const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, page)
const PresentationDemo = (props) => <Presentation docMeta={docMeta} presentationRegistry={registry}/>

export default PresentationDemo
