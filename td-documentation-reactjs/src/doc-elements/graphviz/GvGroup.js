import React, {Component} from 'react'
import {removeCustomProps} from './gvUtils'

class GvGroup extends Component {
    render() {
        const cleanedUpProps = removeCustomProps(this.props)

        const style = {}
        return <g {...cleanedUpProps} style={style}>
            {this.props.children}
        </g>
    }
}

export default GvGroup