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
