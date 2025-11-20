/*
 * Copyright 2021 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'
import SvgCustomShape from './SvgCustomShape'
import { buildUniqueId, removeRadiusPropsNoCopy, removeCustomPropsNoCopy } from "./gvUtils";

import LinkWrap from './LinkWrap'

class GvPolygon extends React.Component {
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
        const sizes = calculateSizes(this.props)
        const colorsOverride = createColors(this.props.parentClassName, this.props.colors)

        if (this.props.svg) {
            const style = createGlowStyle(this.props.diagramId)
            return <SvgCustomShape {...this.props} {...sizes} style={style}/>
        } else {
            const style = createNodeOnlyStyle(this.props.diagramId, this.props.parentClassName)
            const cleanedUpProps = {...this.props}

            removeCustomPropsNoCopy(cleanedUpProps)

            // came from circle dot
            if (this.props.hasOwnProperty("rx") &&
              (Math.abs(this.props.rx - this.props.ry) < 0.0001)) {
                return <ellipse {...cleanedUpProps} {...colorsOverride}/>
            }

            removeRadiusPropsNoCopy(cleanedUpProps)

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
function calculateSizes(props) {
    // handle case of ellipse
    if (props.rx) {
        const {cx, cy, rx, ry} = props
        return {
            cx, cy,
            width: rx * 2.0,
            height: ry * 2.0,
            x: cx - rx,
            y: cy - ry
        }
    }

    const {points} = props
    const coordPairs = points.split(' ')

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