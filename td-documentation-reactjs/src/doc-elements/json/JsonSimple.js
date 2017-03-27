import React from 'react'

const JsonSimple = ({data, path, highlightedPaths}) => {
    const className = "json simple" + (highlightedPaths.hasOwnProperty(path) ?
            " highlighted" : "")

    return <span className={className}>{data}</span>
}

export default JsonSimple
