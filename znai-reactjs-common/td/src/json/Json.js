import React from 'react'
import JsonValue from './JsonValue'

const Json = ({data, paths}) => {
    const highlightedPathsDict = {}
    if (paths) {
        paths.forEach(p => { highlightedPathsDict[p] = true })
    }

    return <div className="json start content-block">
        <JsonValue path={"body"} data={data} highlightedPaths={highlightedPathsDict}/>
    </div>
}

export default Json