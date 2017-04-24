import React from 'react'

import './Table.css'
import 'semantic-ui/dist/components/table.min.css'

const Table = ({table, elementsLibrary}) => {
    const Row = ({row}) => {
        return (<tr>
            {row.map((v, idx) => {
                const c = table.columns[idx]
                const align = c.align ? c.align : "left"
                const width = c.width ? c.width : "auto"

                const style = {textAlign: align, width: width}
                const value = Array.isArray(v) ? <elementsLibrary.DocElement content={v}/> : v

                return (<td key={idx} style={style}>{value}</td>)
            })}
        </tr>)
    }

    return (<div className="simple-table">
        <table className="ui celled padded table">
            <thead>
                <tr>
                    {table.columns.map((c, idx) => {
                        const align = c.align ? c.align : "left"
                        const style = {textAlign: align}
                        return (<th key={idx} style={style}>{c.title}</th>)
                    })}
                </tr>
            </thead>
            <tbody>
                {table.data.map((r, idx) => <Row key={idx} row={r}/>)}
            </tbody>
        </table>
    </div> )
}

export default Table
