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

            return <polygon {...cleanedUpProps} {...colorsOverride} points={sizes.points}
                            style={style}/>
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

// make 4 points polygon slightly smaller so arrows dont connect with the surface
// points="0,-73.5 0,-109.5 54,-109.5 54,-73.5 0,-73.5
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

    const gap = 4
    x[0] += gap
    x[1] += gap
    x[2] -= gap
    x[3] -= gap
    x[4] += gap

    y[0] -= gap
    y[1] += gap
    y[2] += gap
    y[3] -= gap
    y[4] -= gap

    // naive handling of rects only. Need to try to use transform scale
    const newPoints = coordPairs.length !== 5 ? points : `${x[0]},${y[0]} ${x[1]},${y[1]} ${x[2]},${y[2]} ${x[3]},${y[3]} ${x[4]},${y[4]}`
    return {points: newPoints, cx: cx, cy: cy, width: width, height: height}
}

export default GvPolygon