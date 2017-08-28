import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'

import {Columns} from './Columns'

const testData = {
    config: {
        border: true,
        left: {portion: 3}
    },
    columns: [{
        content: [
            {
                "text": `It_is_very_`,
                // "text": `It_is_very_easy_to_add_a_code_snippet_or_an_output_result_It_is_very_easy_to_add_a_code_snipp.`,
                "type": "SimpleText"
            },
        ]
    },
        {
            content: [
                {
                    "text": "one liner",
                    "type": "SimpleText"
                }
            ]
        }]
}


const ColumnsDemo = () => {
    return (
        <Columns elementsLibrary={elementsLibrary} {...testData}/>
    )
}

export default ColumnsDemo
