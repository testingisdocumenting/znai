import React from 'react'
import JsonValue from './JsonValue'

const Json = ({data, highlightedPaths}) => {
    return <div className="json start">
        <JsonValue path={"body"} data={data} highlightedPaths={highlightedPaths || {}}/>
    </div>
}

export default Json