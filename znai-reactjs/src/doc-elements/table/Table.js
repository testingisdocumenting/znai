import React from 'react'

import './Table.css'
import 'semantic-ui-css/components/table.min.css'

const Table = ({table, ...props}) => {
    const tableStyles = table.styles || []

    const Row = ({row}) => {
        return (<tr>
            {row.map((v, idx) => {
                const c = table.columns[idx]
                const align = c.align ? c.align : 'left'
                const width = c.width ? c.width : 'auto'

                const style = {textAlign: align, width: width}
                const value = Array.isArray(v) ? <props.elementsLibrary.DocElement {...props} content={v}/> : v

                return (<td key={idx} style={style}>{value}</td>)
            })}
        </tr>)
    }

    const showHeader = tableStyles.indexOf('no-header') === -1

    // header related style will not trigger custom css
    const isCustomClassName = (tableStyles.length > 0 && showHeader) || (!showHeader && tableStyles.length > 1)

    const tableClassName = (isCustomClassName ? tableStyles.join(' ') :
        'ui celled padded table') + ' content-block';

    return (
        <div className="simple-table content-block">
            <table className={tableClassName}>
                <thead>
                <tr>
                    {showHeader ? table.columns.map((c, idx) => {
                        const align = c.align ? c.align : 'left'
                        const style = {textAlign: align}
                        return (<th key={idx} style={style}>{c.title}</th>)
                    }) : null}
                </tr>
                </thead>
                <tbody>
                {table.data.map((r, idx) => <Row key={idx} row={r}/>)}
                </tbody>
            </table>
        </div>
    )
}

export default Table
