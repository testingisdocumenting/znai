import React from 'react'

import './JupyterHtmlCell.css'

const JupyterHtmlCell = ({html, elementsLibrary}) => {
    return (
        <div className="jupyter-cell jupyter-html content-block">
            <div dangerouslySetInnerHTML={{__html: html}}/>
        </div>
    )
}

export default JupyterHtmlCell
