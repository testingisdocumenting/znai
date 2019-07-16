import React from 'react'

const JupyterImgCell = ({img, elementsLibrary}) => {
    return (
        <div className="jupyter-cell jupyter-img content-block">
            <img src={"data:image/png;base64," + img}/>
        </div>
    )
}

export default JupyterImgCell
