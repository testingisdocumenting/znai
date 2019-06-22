import React, { Component } from 'react'

class SvgCustomShape extends Component {
    render() {
        const {cx, cy, svg, colors, style} = this.props
        const transform = `translate(${cx} ${cy})`
        const localStyle = colors ? {stroke: colors.line, fill: colors.fill} : {}
        const combinedStyle = {...style, ...localStyle}

        return <g transform={transform} style={combinedStyle} dangerouslySetInnerHTML={{__html: svg}}/>
    }
}

export default SvgCustomShape