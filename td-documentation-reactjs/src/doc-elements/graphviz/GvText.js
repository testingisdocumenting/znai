import React, { Component } from 'react'
import {removeCustomProps} from './gvUtils'

class GvText extends Component {
    render() {
        const {colors, isInversedTextColor} = this.props
        const cleanedUpProps = removeCustomProps(this.props)

        const fill = isInversedTextColor ? colors.inversedText : colors.text

        return <text {...cleanedUpProps} fill={fill}>
            {this.props.children[0]}
        </text>
    }
}

export default GvText