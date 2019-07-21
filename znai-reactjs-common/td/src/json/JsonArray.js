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
