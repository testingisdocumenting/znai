import React from 'react'
import JsonValue from './JsonValue'

const JsonObject = ({data, path, highlightedPaths}) => {
    return <div className="json object">
        {Object.keys(data).map(key => {
            const newPath = path + "." + key

            return <div key={key} className="entry">
                <span className="key">{key}</span>
                <span className="value">
                    <JsonValue data={data[key]}
                               path={newPath}
                               highlightedPaths={highlightedPaths}/>
                </span>
            </div>
        })}
    </div>
}

export default JsonObject
