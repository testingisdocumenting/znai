import React, { Component } from 'react'
import styleMeta from './gvStyleMeta'

class GvText extends Component {
    render() {
        const {colors} = this.props
        return <text {...this.props} fontFamily="verdana" fontSize="8" fill={colors.text}>
            {styleMeta.removeStyleNames(this.props.children[0])}
        </text>
    }
}

export default GvText