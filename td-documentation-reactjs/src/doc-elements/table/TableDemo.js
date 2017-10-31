import React from 'react'

import {elementsLibrary} from '../DefaultElementsLibrary'
import Table from './Table'

const columns = [
    {title: 'Column 1', align: 'right'},
    {title: 'Column 2', width: '30%'}
]

const data = [
    [1, 2],
    [3, 4],
    ['hello', [
        {
            'text': 'We saw (todo link) how you can annotate images using ',
            'type': 'SimpleText'
        },
        {
            'code': 'include-image',
            'type': 'InlinedCode'
        },
        {
            'text': ' plugin.',
            'type': 'SimpleText'
        },
        {
            'type': 'SoftLineBreak'
        },
        {
            'text': 'Now let\u0027s automate the screenshot and annotations assigning process.',
            'type': 'SimpleText'
        }
    ]
    ],
]


const table1 = {
    styles: [],
    columns: columns, data: data
}

const table2 = {
    styles: ['no-header', 'middle-vertical-lines-only'],
    columns: columns, data: data
}

const table3 = {
    styles: ['no-header', 'middle-vertical-lines-only', 'no-vertical-padding'],
    columns: columns, data: data
}

const TableDemo = () => {
    return (
        <div>
            <Table table={table1} elementsLibrary={elementsLibrary}/>
            <Table table={table2} elementsLibrary={elementsLibrary}/>
            <Table table={table3} elementsLibrary={elementsLibrary}/>
        </div>
    )
}

export default TableDemo