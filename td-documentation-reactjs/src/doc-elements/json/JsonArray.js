import React from 'react'
import JsonValue from './JsonValue'

const JsonArray = ({data, path, highlightedPaths}) => {
    const arrayValues = data.map((v, idx) => {
        const newPath = path + "[" + idx + "]"
        const isLast = idx === data.length - 1

        const value = <JsonValue key={newPath}
                                 data={v}
                                 path={newPath}
                                 highlightedPaths={highlightedPaths}/>
        return isLast ? value : [value, <span className="delimiter">, </span>]
    })

    arrayValues.unshift(<span key="__start" className="delimiter">[</span>)
    arrayValues.push(<span key="__end" className="delimiter">]</span>)

    return <div className="json array">
        {arrayValues}
    </div>
}

export default JsonArray
