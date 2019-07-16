import React from 'react'
import JupyterCodeCell from './JupyterCodeCell'
import JupyterHtmlCell from './JupyterHtmlCell'
import JupyterTextCell from './JupyterTextCell'
import JupyterEmptyCell from './JupyterEmptyCell'
import JupyterSvgCell from './JupyterSvgCell'
import JupyterImgCell from './JupyterImgCell'

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

    if (cell.svg) {
        return JupyterSvgCell
    }

    if (cell.img) {
        return JupyterImgCell
    }

    return JupyterEmptyCell
}

export default JupyterCell
