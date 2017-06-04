import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'


const noteParagraph = createMessage("Note:")
const warningParagraph = createMessage("Warning:")
const avoidParagraph = createMessage("Avoid:")
const questionParagraph = createMessage("Question:")

function createMessage(suffix) {
    return {
        "type": "Paragraph",
        "content": [
        {
            "text": `${suffix} It is very easy to add a code snippet or an output result.`,
            "type": "SimpleText"
        },
        {
            "type": "SoftLineBreak"
        },
        {
            "text": "All you have to do is indent your code with 4 spaces inside your markdown document and",
            "type": "SimpleText"
        },
        {
            "type": "SoftLineBreak"
        },
        {
            "text": "your code will be rendered like this.",
            "type": "SimpleText"
        }
    ]
    }

}

const ParagraphDemo = () => {
    const content = [noteParagraph, warningParagraph, avoidParagraph, questionParagraph]
    return <elementsLibrary.DocElement content={content} elementsLibrary={elementsLibrary}/>
}

export default ParagraphDemo
