import React from 'react'
import JsonQuoted from './JsonQuoted'
import JsonComma from './JsonComma'

const JsonSimple = ({data, path, includeComma, highlightedPaths}) => {
    const className = "json simple" + (highlightedPaths.hasOwnProperty(path) ?
            " highlighted" : "") + " " + classByType(data)

    if (typeof data === 'string') {
        return <JsonQuoted className={className} data={data} includeComma={includeComma}/>
    }

    const dataWithComma = includeComma ? [data, <JsonComma/>] : data
    return <span className={className}>{dataWithComma}</span>
}

function classByType(data) {
    return typeof data
}

export default JsonSimple
