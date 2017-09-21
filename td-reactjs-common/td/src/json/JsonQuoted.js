import React from 'react'
import JsonComma from './JsonComma'

const JsonQuoted = ({className, data, includeComma}) => {
    const comma = includeComma ? <JsonComma/> : null
    return <span className={className}>
        <span className="delimiter quote">"</span>
        {escapeQuote(data)}
        <span className="delimiter quote">"</span>
        {comma}
    </span>
}

function escapeQuote(data) {
    if (! data) {
        return data
    }

    return data.replace(/"/g, "\\\"")
}

export default JsonQuoted
