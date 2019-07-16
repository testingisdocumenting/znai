import React from 'react'

const SvgWithCalculatedSize = ({isPresentation, viewBox, children}) => {
    const className = isPresentation ? "" : "content-block"
    const style = isPresentation ? {width: "100vw"} : {}

    return (
        <div className={className} style={style}>
            <svg width="100%" height="100%" viewBox={viewBox}>
                {children}
            </svg>
        </div>
    )
}

export default SvgWithCalculatedSize
