import React from 'react'

import SimpleTable from './SimpleTable'

const table = {
    columns: [
        {title: "Column 1", align: "left"},
        {title: "Column 2", width: 300}
    ], data: [
        [1, 2],
        [3, 4],
        ["hello", "world"],
    ]
}

const TableDemo = () => {
    return <SimpleTable table={table}/>
}

export default TableDemo