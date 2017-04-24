import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'
import Table from './Table'

const table = {
    columns: [
        {title: "Column 1", align: "right"},
        {title: "Column 2", width: 300}
    ], data: [
        [1, 2],
        [3, 4],
        ["hello", [
            {
                "text": "We saw (todo link) how you can annotate images using ",
                "type": "SimpleText"
            },
            {
                "code": "include-image",
                "type": "InlinedCode"
            },
            {
                "text": " plugin.",
                "type": "SimpleText"
            },
            {
                "type": "SoftLineBreak"
            },
            {
                "text": "Now let\u0027s automate the screenshot and annotations assigning process.",
                "type": "SimpleText"
            }
        ]
        ],
    ]
}

const TableDemo = () => {
    return <Table table={table} elementsLibrary={elementsLibrary}/>
}

export default TableDemo