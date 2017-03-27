import React, {Component} from 'react'
import JsonArray from './JsonArray'
import JsonSimple from './JsonSimple'
import JsonObject from './JsonObject'

import './JsonValue.css'

class JsonValue extends Component {
    render() {
        const {data, highlightedPaths} = this.props
        const path = this.props.path || "body"

        const Component = componentTypeForData(data)
        return <Component data={data} path={path} highlightedPaths={highlightedPaths || {}}/>
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
