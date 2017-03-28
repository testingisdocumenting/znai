import React from 'react'
import JsonValue from './JsonValue'

const JsonArray = ({data, path, highlightedPaths}) => {
    const isObjects = data.filter(v => typeof v === 'object').length > 0

    const arrayValues = data.map((v, idx) => {
        const newPath = path + "[" + idx + "]"
        const isLast = idx === data.length - 1

        const value = <JsonValue key={newPath}
                                 data={v}
                                 path={newPath}
                                 includeComma={! isLast}
                                 highlightedPaths={highlightedPaths}/>
        return isLast ? value : [value,
            <span className="delimiter optional-enter"/>]
    })

    arrayValues.unshift(<span key="__start" className="delimiter open-bracket">[</span>)
    arrayValues.push(<span key="__end" className="delimiter close-bracket">]</span>)

    const className = "json array" + (isObjects ? " objects" : "")
    return <div className={className}>
        {arrayValues}
    </div>
}

export default JsonArray
