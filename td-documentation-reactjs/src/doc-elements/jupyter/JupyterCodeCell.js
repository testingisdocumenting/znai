import React from 'react'

import './JupyterCodeCell.css'

const JupyterCodeCell = ({snippet, lang, elementsLibrary}) => {
    return (
        <div className="jupyter-cell jupyter-code">
            <elementsLibrary.Snippet snippet={snippet} lang={lang}/>
        </div>
    )
}

export default JupyterCodeCell
