/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        objectValues.push(<JsonComma key="comma"/>)
    }

    return <div className="json object">
        {objectValues}
    </div>
}

export default JsonObject
