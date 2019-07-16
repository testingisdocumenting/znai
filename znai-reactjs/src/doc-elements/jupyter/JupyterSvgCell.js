import React from 'react'

const JupyterSvgCell = ({svg, elementsLibrary}) => {
    return (
        <div className="jupyter-cell jupyter-svg content-block">
            <elementsLibrary.Svg svg={svg}/>
        </div>
    )
}

export default JupyterSvgCell
