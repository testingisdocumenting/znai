import React from 'react'

const JupyterCodeCell = ({sourceTokens, elementsLibrary}) => {
    return (
        <div className="jupyter-code">
            <elementsLibrary.Snippet tokens={sourceTokens}/>
        </div>
    )
}

export default JupyterCodeCell
