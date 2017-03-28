import React from 'react'
import JsonValue from './JsonValue'
import JsonQuoted from './JsonQuoted'
import JsonComma from './JsonComma'

const JsonObject = ({data, path, highlightedPaths, includeComma}) => {
    const keys = Object.keys(data)
    const objectValues = keys.map((key, idx) => {
        const newPath = path + "." + key
        const isLast = idx === keys.length - 1

        const value = <JsonValue key={key}
                                 data={data[key]}
                                 path={newPath}
                                 includeComma={! isLast}
                                 highlightedPaths={highlightedPaths}/>

        return <div key={key} className="entry">
            <JsonQuoted className="key" data={key}/>
            <span className="delimiter colon">: </span>
            <span className="value">{value}</span>
        </div>
    })

    objectValues.unshift(<span key="__start" className="delimiter open-curly">&#123;</span>)
    objectValues.push(<span key="__end" className="delimiter close-curly">&#125;</span>)
    if (includeComma) {
        objectValues.push(<JsonComma/>)
    }

    return <div className="json object">
        {objectValues}
    </div>
}

const t = {
    "key1"
        :
        "value1",
    "key2"
        :
        {
            "key21"
                :
                "value21",
            "key22"
                :
                "value22",
            "key23"
                :
                {
                    "key231"
                        :
                        "value231"
                }
        },
    "key3"
        :
        {
            "key31"
                :
                "value31",
            "key32"
                :
                "value32"
        }
}

export default JsonObject
