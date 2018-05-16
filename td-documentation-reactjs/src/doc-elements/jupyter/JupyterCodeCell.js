import React from 'react'

import './JupyterCodeCell.css'

const JupyterCodeCell = ({sourceTokens, elementsLibrary}) => {
    return (
        <div className="jupyter-cell jupyter-code">
            <elementsLibrary.Snippet tokens={sourceTokens}/>
        </div>
    )
}

export default JupyterCodeCell
