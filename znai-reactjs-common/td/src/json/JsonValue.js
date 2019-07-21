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

import React, {Component} from 'react'
import JsonArray from './JsonArray'
import JsonSimple from './JsonSimple'
import JsonObject from './JsonObject'

import './JsonValue.css'

class JsonValue extends Component {
    render() {
        const {data, path, highlightedPaths, includeComma} = this.props

        const Component = componentTypeForData(data)
        return <Component data={data} path={path} includeComma={includeComma} highlightedPaths={highlightedPaths}/>
    }
}

function componentTypeForData(data) {
    if (Array.isArray(data)) {
        return JsonArray
    } else if (typeof data === 'object') {
        return JsonObject
    } else {
        return JsonSimple
    }
}

export default JsonValue
