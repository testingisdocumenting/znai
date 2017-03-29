import React from 'react'
import JsonValue from './JsonValue'

const Json = ({data, highlightedPaths}) => {
    const highlightedPathsDict = {}
    if (highlightedPaths) {
        highlightedPaths.forEach(p => { highlightedPathsDict[p] = true })
    }

    return <div className="json start">
        <JsonValue path={"body"} data={data} highlightedPaths={highlightedPathsDict}/>
    </div>
}

export default Json