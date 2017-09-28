import React from 'react'
import Json from '../json/Json'

const JsonPayload = ({data, checks}) => {
    const paths = checks && checks.passedPaths ? checks.passedPaths : []
    return (
        <Json data={JSON.parse(data)} paths={paths}/>
    )
}

const RestPayload = ({caption, type, data, checks}) => {
    if (! data) {
        return null
    }

    return (
        <div className="rest-payload">
            <div className="caption">{caption}</div>
            <JsonPayload data={data} checks={checks}/>
        </div>
    )
}

export default RestPayload
