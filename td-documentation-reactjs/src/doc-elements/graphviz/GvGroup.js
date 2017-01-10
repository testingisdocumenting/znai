import React, { Component } from 'react'
import gvUtils from './gvUtils'

class GvGroup extends Component {
    render() {
        const cleanedUpProps = gvUtils.removeCustomProps(this.props)
        return <g {...cleanedUpProps}>
            {this.props.children}
        </g>
    }
}

export default GvGroup