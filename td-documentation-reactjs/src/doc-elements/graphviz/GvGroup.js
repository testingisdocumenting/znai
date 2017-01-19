import React, { Component } from 'react'
import {removeCustomProps, buildUniqueId} from './gvUtils'

class GvGroup extends Component {
    render() {
        const {selected} = this.props
        const cleanedUpProps = removeCustomProps(this.props)

        const style = selected ? {filter: `url(#${buildUniqueId(this.props.diagramId, "highlight_filter")})` } : {}
        return <g {...cleanedUpProps} style={style}>
            {this.props.children}
        </g>
    }
}

export default GvGroup