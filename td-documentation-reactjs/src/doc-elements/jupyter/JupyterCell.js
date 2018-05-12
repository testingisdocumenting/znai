import React from 'react'
import JupyterCodeCell from './JupyterCodeCell'
import JupyterHtmlCell from './JupyterHtmlCell'
import JupyterTextCell from './JupyterTextCell'

const JupyterCell = (props) => {
    const Cell = cellComponent(props)
    return (
        <Cell {...props} elementsLibrary={props.elementsLibrary}/>
    )
}

function cellComponent(cell) {
    if (cell.sourceTokens) {
        return JupyterCodeCell
    }

    if (cell.text) {
        return JupyterTextCell
    }

    if (cell.html) {
        return JupyterHtmlCell
    }

    return JupyterCodeCell
}

export default JupyterCell
