import React, { Component } from 'react'

class GvGroup extends Component {
    render() {
        return <g {...this.props}>
            {this.props.children}
        </g>
    }
}

export default GvGroup