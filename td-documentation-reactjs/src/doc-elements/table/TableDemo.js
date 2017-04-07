import React from 'react'

import SimpleTable from './SimpleTable'

const table = {
    columns: [
        {title: "Column 1", align: "right"},
        {title: "Column 2", width: 300}
    ], data: [
        [1, 2],
        [3, 4],
        ["hello", "world"],
    ]
}

const TableDemo = () => {
    return <Table table={table}/>
}

export default TableDemo