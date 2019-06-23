import React, {Component} from 'react'
import SvgCustomShape from './SvgCustomShape'
import {removeCustomProps, buildUniqueId} from './gvUtils'

import LinkWrap from './LinkWrap'

class GvPolygon extends Component {
    render() {
        const {url} = this.props
        const renderNode = this.renderNode()

        return (
            <LinkWrap url={url}>
                {renderNode}
            </LinkWrap>
        )
    }

    renderNode() {
        const sizes = calculateSizes(this.props.points)
        const colorsOverride = createColors(this.props.parentClassName, this.props.colors)

        if (this.props.svg) {
            const style = createGlowStyle(this.props.diagramId)
            return <SvgCustomShape {...this.props} {...sizes} style={style}/>
        } else {
            const style = createNodeOnlyStyle(this.props.diagramId, this.props.parentClassName)
            const cleanedUpProps = removeCustomProps(this.props)

            if (this.props.parentClassName === 'node') {
                const gap = 4;
                return <rect {...cleanedUpProps} {...colorsOverride}
                             x={sizes.x + gap} y={sizes.y + gap}
                             width={sizes.width - gap * 2} height={sizes.height - gap * 2}
                             style={style}/>
            }

            return <polygon points={this.props.points} {...cleanedUpProps} {...colorsOverride} style={style}/>
        }
    }
}

function createColors(parentClassName, colors) {
    if (parentClassName === 'graph') {
        return {}
    }

    return {fill: colors.fill, stroke: colors.line}
}

function createNodeOnlyStyle(diagramId, parentClassName) {
    if (parentClassName === 'node') {
        return createGlowStyle(diagramId)
    }

    return {}
}

function createGlowStyle(diagramId) {
    return {filter: `url(#${buildUniqueId(diagramId, "glow_filter")})`}
}

// make polygon slightly smaller so arrows dont connect with the surface
// calculates center and sizes
function calculateSizes(points) {
    let coordPairs = points.split(' ')

    let x = []
    let y = []

    const collectXY = (pair) => {
        const split = pair.split(',')
        x.push(Number(split[0]))
        y.push(Number(split[1]))
    }

    coordPairs.forEach((pair) => collectXY(pair))
    const minX = Math.min(...x)
    const maxX = Math.max(...x)
    const minY = Math.min(...y)
    const maxY = Math.max(...y)

    const cx = (minX + maxX) / 2.0
    const cy = (minY + maxY) / 2.0
    const width = Math.abs(minX - maxX)
    const height = Math.abs(minY - maxY)

    return {cx: cx, cy: cy, x: cx - width/2.0, y: cy - height/2.0, width: width, height: height}
}

export default GvPolygon