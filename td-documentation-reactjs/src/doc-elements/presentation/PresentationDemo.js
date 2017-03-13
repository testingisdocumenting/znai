import React, {Component} from 'react'

import Presentation from './Presentation'
import {presentationRegistry} from './PresentationRegistry'
import PresentationAnnotatedImage from '../images/PresentationAnnotatedImage.js'
import Snippet from '../default-elements/Snippet'

const props1 = {
    "imageSrc": "screenshot1.png",
    "shapes": [
        {
            "type": "circle",
            "id": "c1",
            "x": 100.0,
            "y": 100.0,
            "r": 20.0,
            "text": "1",
            "color": "blue"
        },
        {
            "type": "circle",
            "id": "c2",
            "x": 250.0,
            "y": 100.0,
            "r": 20.0,
            "text": "2",
            "color": "blue"
        }
    ]
}

const props2 = {
    "imageSrc": "screenshot1.png",
    "shapes": [
        {
            "type": "arrow",
            "id": "a1",
            "beginX": 200.0,
            "beginY": 100.0,
            "endX": 48.0,
            "endY": 42.0,
            "color": "red",
            "text": "debug"
        }
    ]
}

const props3 = {
    "lang": "json",
    "maxLineLength": 55,
    "type": "Snippet",
    "tokens": [
        {
            "type": "punctuation",
            "data": "["
        },
        {
            "type": "punctuation",
            "data": "{"
        },
        {
            "type": "property",
            "data": "\"type\""
        },
        {
            "type": "operator",
            "data": ":"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "string",
            "data": "\"arrow\""
        },
        {
            "type": "punctuation",
            "data": ","
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "property",
            "data": "\"id\""
        },
        {
            "type": "operator",
            "data": ":"
        },
        {
            "type": "string",
            "data": "\"a1\""
        },
        {
            "type": "punctuation",
            "data": ","
        },
        {
            "type": "text",
            "data": "\n  "
        },
        {
            "type": "property",
            "data": "\"beginX\""
        },
        {
            "type": "operator",
            "data": ":"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "number",
            "data": "200"
        },
        {
            "type": "punctuation",
            "data": ","
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "property",
            "data": "\"beginY\""
        },
        {
            "type": "operator",
            "data": ":"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "number",
            "data": "100"
        },
        {
            "type": "punctuation",
            "data": ","
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "property",
            "data": "\"endX\""
        },
        {
            "type": "operator",
            "data": ":"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "number",
            "data": "48"
        },
        {
            "type": "punctuation",
            "data": ","
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "property",
            "data": "\"endY\""
        },
        {
            "type": "operator",
            "data": ":"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "number",
            "data": "42"
        },
        {
            "type": "punctuation",
            "data": ","
        },
        {
            "type": "text",
            "data": "\n  "
        },
        {
            "type": "property",
            "data": "\"color\""
        },
        {
            "type": "operator",
            "data": ":"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "string",
            "data": "\"red\""
        },
        {
            "type": "punctuation",
            "data": ","
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "property",
            "data": "\"text\""
        },
        {
            "type": "operator",
            "data": ":"
        },
        {
            "type": "text",
            "data": " "
        },
        {
            "type": "string",
            "data": "\"debug\""
        },
        {
            "type": "punctuation",
            "data": "}"
        },
        {
            "type": "punctuation",
            "data": "]"
        }
    ]
}


// presentationRegistry.register(PresentationAnnotatedImage, props1, 2)
// presentationRegistry.register(PresentationAnnotatedImage, props2, 1)
presentationRegistry.register(Snippet, props3, 1)

const PresentationDemo = (props) => <Presentation/>

export default PresentationDemo

