import React, {Component} from 'react'
import {removeCustomProps} from './gvUtils'

import LinkWrap from './LinkWrap'

class GvText extends Component {
    render() {
        const {colors, isInversedTextColor, url} = this.props
        const cleanedUpProps = removeCustomProps(this.props)

        const fill = isInversedTextColor ? colors.inversedText : colors.text

        return (
            <LinkWrap url={url}>
                <text {...cleanedUpProps} fill={fill}>
                    {this.props.children[0]}
                </text>
            </LinkWrap>
        )
    }
}

export default GvText