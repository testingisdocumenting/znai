import React, { Component } from 'react'

class SvgCustomShape extends Component {
    render() {
        const {width, height, cx, cy, svg, colors} = this.props
        const transform = `translate(${cx} ${cy})`
        const style = colors ? {stroke: colors.line, fill: colors.fill} : {}

        return <g transform={transform} style={style} dangerouslySetInnerHTML={{__html: svg}}/>
    }
}

export default SvgCustomShape