import React from 'react'

import {elementsLibrary, presentationElementHandlers} from '../DefaultElementsLibrary'

import Presentation from './Presentation'
import PresentationRegistry from './PresentationRegistry'

const content = [
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

const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, content)
const PresentationDemo = (props) => <Presentation presentationRegistry={registry}/>

export default PresentationDemo

