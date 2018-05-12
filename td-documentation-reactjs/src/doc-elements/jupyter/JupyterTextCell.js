import React from 'react'

const JupyterTextCell = ({text, elementsLibrary}) => {
    return (
        <div className="jupyter-text content-block">
            <pre>
                {text}
            </pre>
        </div>
    )
}

export default JupyterTextCell
